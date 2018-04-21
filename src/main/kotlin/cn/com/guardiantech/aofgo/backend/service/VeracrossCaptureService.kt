package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.authentication.AuthContext
import cn.com.guardiantech.aofgo.backend.data.entity.veracross.VeracrossCookie
import cn.com.guardiantech.aofgo.backend.exception.CannotLoginException
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.repository.veracross.VeracrossCookieRepo
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import javax.annotation.PostConstruct

@Service
class VeracrossCaptureService @Autowired constructor(
        @Autowired
        val studentRepo: StudentRepository,
        @Autowired
        val veracrossCookieRepo: VeracrossCookieRepo
) {

    companion object {
        private val logger = LoggerFactory.getLogger(VeracrossCaptureService::class.java)
    }

    private val rest = RestTemplate()

    @PostConstruct
    fun prepareRestTemplate() {

    }

    fun authenticate(username: String, password: String, subjectId: Long) {
        val authHome = rest.exchange("https://portals.Veracross.com/aof/login", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)
        if (authHome.statusCode == HttpStatus.OK) {
            val loginPage = Jsoup.parse(authHome.body).body()
            val loginForm = loginPage.getElementsByTag("form").first()

            val authenticity_token = loginForm.getElementsByAttributeValue("name", "authenticity_token").`val`()
            val return_to = loginForm.getElementsByAttributeValue("name", "return_to").`val`()
            // post to this address the authenticity_token
            val application = loginForm.getElementsByAttributeValue("name", "application").`val`()
            val remote_ip = loginForm.getElementsByAttributeValue("name", "remote_ip").`val`()
            val submit_button = loginForm.getElementsByAttributeValue("name", "commit").`val`()

            val destURL = loginForm.attr("action")
            val submissionForm = LinkedMultiValueMap<String, String>()
            submissionForm.add("authenticity_token", authenticity_token)
            submissionForm.add("return_to", return_to)
            submissionForm.add("application", application)
            submissionForm.add("remote_ip", remote_ip)
            submissionForm.add("commit", submit_button)
            submissionForm.add("username", username)
            submissionForm.add("password", password)

            var headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

            val authJump = rest.postForEntity(destURL, HttpEntity<MultiValueMap<String, String>>(submissionForm, headers), String::class.java)

            val authJumpForm = Jsoup.parse(authJump.body).body().getElementsByTag("form").first()
            val authJumpDest = authJumpForm.attr("action")
            val accountSecret = authJumpForm.getElementsByAttributeValue("name", "account").`val`()

            val secondPageSubmissionPage = LinkedMultiValueMap<String, String>()
            secondPageSubmissionPage.add("authenticity_token", authenticity_token)
            secondPageSubmissionPage.add("account", accountSecret)

            val sessionJump = rest.postForEntity(authJumpDest, HttpEntity<MultiValueMap<String, String>>(secondPageSubmissionPage, headers), String::class.java)

            val authCookie = sessionJump.headers.get("Set-VeracrossCookie")

            var veracrossCookie: VeracrossCookie? = null
            authCookie?.let {
                veracrossCookie = VeracrossCookie(
                        session = it[0],
                        student = studentRepo.findStudentBySubjectId(subjectId).get()
                )
            }
            if (veracrossCookie != null) {
                veracrossCookieRepo.save(veracrossCookie)
            }
        }
        throw CannotLoginException("Cannot access Veracross: https://portals.Veracross.com/aof/login")
    }

    fun extractCourseData(cookie: MutableList<String>?): String {
        var headers = HttpHeaders()
        headers.set("cookie", cookie)
        headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")

        val vHome = rest.exchange("https://portals-app.Veracross.com/aof/home", HttpMethod.GET, HttpEntity<Unit>(headers), String::class.java)
        val vHomePage = Jsoup.parse(vHome.body)
        val csrf_token = vHomePage.getElementsByAttributeValue("name", "csrf-token").first().attr("content")

        headers.set("x-csrf-token", csrf_token)
        headers.set("cookie", vHome.headers.get("Set-VeracrossCookie"))
        headers.set("Content-Encoding", "gzip")
        headers.set("Content-Type", "application/json; charset=utf-8")

        val courseData = rest.exchange("https://portals.Veracross.com/aof/component/ClassListStudent/1308/load_data", HttpMethod.POST, HttpEntity<Unit>(headers), String::class.java)
        val courseJson = courseData.body
        // You get a json, how nice is that

        return courseJson
    }

    fun extractCalendarData(cookie: MutableList<String>?): String {
        var headers = HttpHeaders()
        headers.set("VeracrossCookie", cookie)
        headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")

        val vCal = rest.exchange("https://portals.Veracross.com/aof/student/calendar/student", HttpMethod.GET, HttpEntity<Unit>(headers), String::class.java)
        val vCalPage = Jsoup.parse(vCal.body)
        val csrf_token = vCalPage.getElementsByAttributeValue("name", "csrf-token").first().attr("content")

        val dateUrlParam = generateCalendarParams()

        headers.set("x-csrf-token", csrf_token)
        headers.set("Content-Type", "application/json")

        val url = UriComponentsBuilder.fromHttpUrl("https://portals.Veracross.com/aof/student/calendar/student/events")
        dateUrlParam.forEach { k, v ->
            url.queryParam(k, v)
        }

        val calData = rest.exchange(url.toUriString(), HttpMethod.GET, HttpEntity<Unit>(headers), String::class.java)
        val calJson = calData.body
        // You get a json again, how nice is that

        return calJson
    }

    fun generateCalendarParams(): Map<String, String> {
        // calculate the url param for the next 30 days, to be sent to the Veracross API
        var calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.MONTH, 1)
        val endYear = calendar.get(Calendar.YEAR)
        val endMonth = calendar.get(Calendar.MONTH)
        val endDay = calendar.get(Calendar.DAY_OF_MONTH)
        val rtnMap = hashMapOf<String, String>()
        rtnMap["begin_date"] = "$currentMonth/$currentDay/$currentYear"
        rtnMap["end_date"] = "$endMonth/$endDay/$endYear"
        return rtnMap
    }
}