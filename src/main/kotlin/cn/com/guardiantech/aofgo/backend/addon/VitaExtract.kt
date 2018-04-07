package cn.com.guardiantech.aofgo.backend.addon

import cn.com.guardiantech.aofgo.backend.exception.CannotLoginException
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.util.*
import javax.annotation.PostConstruct

@Service
class VitaExtract {

    companion object {
        private val logger = LoggerFactory.getLogger(VitaExtract::class.java)
    }
    private val rest = RestTemplate()

    @PostConstruct
    fun prepareRestTemplate() {

    }

    fun authenticate(username: String, password: String) {
        val authHome = rest.exchange("https://portals.veracross.com/aof/login", HttpMethod.GET, HttpEntity.EMPTY, String::class.java)
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

            headers = HttpHeaders()
            headers.set("cookie", sessionJump.headers.get("Set-Cookie"))

            val vHome = rest.exchange("https://portals-app.veracross.com/aof/home", HttpMethod.GET, HttpEntity<Unit>(headers), String::class.java)

            val vHomePage = Jsoup.parse(vHome.body)

            val csrf_token = vHomePage.getElementsByAttributeValue("name", "csrf-token").first().attr("content")

            headers.set("cookie", vHome.headers.get("set-cookie"))
            headers.set("x-csrf-token", csrf_token)
            headers.set("Content-Encoding", "gzip")
            headers.set("Content-Type", "application/json; charset=utf-8")
            headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")

            val courseData = rest.exchange("https://portals.veracross.com/aof/component/ClassListStudent/1308/load_data", HttpMethod.POST, HttpEntity<Unit>(headers), String::class.java)
            //https://portals.veracross.com/aof/student/calendar/student/events?begin_date=04%2F01%2F2018&end_date=05%2F13%2F2018
            print("\n\n\n\n\n\n\n")
            print(courseData)
            print("\n\n\n\n\n\n\n")

            generateCalendarParams()

        } else {
            throw CannotLoginException("Cannot access Veracross: https://portals.veracross.com/aof/login")
        }
    }

    fun generateCalendarParams(){
        // calculate the url param for the next 30 days, to be sent to the Veracross API
        var calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.MONTH, 1)
        val endYear = calendar.get(Calendar.YEAR)
        val endMonth = calendar.get(Calendar.MONTH)
        val endDay = calendar.get(Calendar.DAY_OF_MONTH)
        print("\n\n\n\n\n\n\n")
        print("begin_date=$currentMonth%2F$currentDay%2F$currentYear&end_date=$endMonth%2F$endDay%2F$endYear")
        print("\n\n\n\n\n\n\n")
    }
}