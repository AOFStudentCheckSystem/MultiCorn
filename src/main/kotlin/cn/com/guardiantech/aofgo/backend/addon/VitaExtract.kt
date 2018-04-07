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
import org.springframework.web.util.UriComponentsBuilder
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

    fun authenticate(username: String, password: String): MutableList<String>? {
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

            val authCookie = sessionJump.headers.get("Set-Cookie")

            return authCookie
        } else {
            throw CannotLoginException("Cannot access Veracross: https://portals.veracross.com/aof/login")
        }
    }

    fun extractCourseData(cookie: MutableList<String>?): String {
        var headers = HttpHeaders()
        headers.set("cookie", cookie)
        headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")

        val vHome = rest.exchange("https://portals-app.veracross.com/aof/home", HttpMethod.GET, HttpEntity<Unit>(headers), String::class.java)
        val vHomePage = Jsoup.parse(vHome.body)
        val csrf_token = vHomePage.getElementsByAttributeValue("name", "csrf-token").first().attr("content")

        headers.set("x-csrf-token", csrf_token)
        headers.set("cookie", vHome.headers.get("Set-Cookie"))
        headers.set("Content-Encoding", "gzip")
        headers.set("Content-Type", "application/json; charset=utf-8")

        val courseData = rest.exchange("https://portals.veracross.com/aof/component/ClassListStudent/1308/load_data", HttpMethod.POST, HttpEntity<Unit>(headers), String::class.java)
        val courseJson = courseData.body
        // You get a json, how nice is that

        return courseJson
    }

    fun extractCalendarData(cookie: MutableList<String>?): String {
        var headers = HttpHeaders()
        headers.set("Cookie", cookie)
        headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")

        val vCal = rest.exchange("https://portals.veracross.com/aof/student/calendar/student", HttpMethod.GET, HttpEntity<Unit>(headers), String::class.java)
        val vCalPage = Jsoup.parse(vCal.body)
        val csrf_token = vCalPage.getElementsByAttributeValue("name", "csrf-token").first().attr("content")

        val dateUrlParam = generateCalendarParams()

        headers.set("x-csrf-token", csrf_token)
        val magic = """
            _veracross_session=MVlUODgwekdzNlNCVnZmU2xMbCtRMlI5MnBqakpsdzVlL1g3ZjJjT2pFMzRnVXBKKzJqajhRS1MrVzVpZHdoTktLaTFtNUNhcEVCL3YzMlhsQ0tDQWZQdmg3V0haTHNKOU9MUVZQdUMzeVU3WnhteG0rQ1RzMlpBbStyNDg4Tk1HZWhpM1pJa2NMeXBEQU9jMUllcS84bzhKbUs1STFpSWRhYXlib2J1bmY3eGJlZnJrS1hWN2VZOGdUTUw0SXY0a2dEVlRjWlJvaStyT2wrNlNxcU0vZG5NbVNJaEtHL3dHNDVEWUpEQ0dHUXBJMTc3OXI4TGlXZkt0TERmcE5xSldBWG1kSXhoTW5lUWZQSVJZTnk5SlE9PS0tbzFISml0d2dnL1BFM1ByOHArNmtJQT09--985913eb2f7ac986c12bdb0b5d58965a53d4db1d; path=/; domain=.veracross.com; Secure; HttpOnly; Expires=Tue, 19 Jan 2038 03:14:07 GMT;
            """.trimIndent()
        headers.set("Cookie", magic)
        headers.set("Content-Type", "application/json")
//        headers.set("Accept-Encoding", "gzip, deflte, br")

        println(headers)
        println("https://portals.veracross.com/aof/student/calendar/student/events?$dateUrlParam")

        val url = UriComponentsBuilder.fromHttpUrl("https://portals.veracross.com/aof/student/calendar/student/events")
        dateUrlParam.forEach { k, v ->
            url.queryParam(k, v)
        }
        println(url.toUriString())

        val calData = rest.exchange(url.toUriString(), HttpMethod.GET, HttpEntity<Unit>(headers), String::class.java)
        val calJson = calData.body
        // You get a json again, how nice is that

        return calJson
    }

    fun generateCalendarParams(): Map<String, String>{
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