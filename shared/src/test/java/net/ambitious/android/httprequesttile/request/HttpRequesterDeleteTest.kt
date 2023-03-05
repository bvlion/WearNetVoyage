package net.ambitious.android.httprequesttile.request

import kotlinx.coroutines.runBlocking
import net.ambitious.android.httprequesttile.data.Constant
import net.ambitious.android.httprequesttile.data.RequestParams
import net.ambitious.android.httprequesttile.util.TestUtil
import org.junit.Assert.assertEquals
import org.junit.Test

class HttpRequesterDeleteTest {

  private val requester = HttpRequester()

  @Test
  fun executeDeleteFormTest() {
    val title = "test_delete_form_anything"
    val parameterKey1 = "parameterKey1"
    val parameterValue1 = "parameterValue1"
    val parameterKey2 = "parameterKey2"
    val parameterValue2 = "parameterValue2"

    val actual = runBlocking {
      requester.execute(RequestParams(
        title,
        "https://httpbin.org/anything",
        Constant.HttpMethod.DELETE,
        Constant.BodyType.FORM_PARAMS,
        null,
        mapOf(
          parameterKey1 to parameterValue1,
          parameterKey2 to parameterValue2,
        )
      ))
    }
    assertEquals(200, actual.responseCode)
    assertEquals(title, actual.title)
    assertEquals(parameterValue1, TestUtil.getFormBody(actual.body, parameterKey1))
    assertEquals(parameterValue2, TestUtil.getFormBody(actual.body, parameterKey2))
    assertEquals(Constant.HttpMethod.DELETE.name, TestUtil.getMethodBody(actual.body))
  }

  @Test
  fun executeDeleteJsonTest() {
    val title = "test_delete_json_anything"
    val parameterKey1 = "parameterKey1"
    val parameterValue1 = "parameterValue1"
    val parameterKey2 = "parameterKey2"
    val parameterValue2 = "parameterValue2"

    val actual = runBlocking {
      requester.execute(RequestParams(
        title,
        "https://httpbin.org/anything",
        Constant.HttpMethod.DELETE,
        Constant.BodyType.JSON,
        null,
        mapOf(
          parameterKey1 to parameterValue1,
          parameterKey2 to parameterValue2,
        )
      ))
    }
    assertEquals(200, actual.responseCode)
    assertEquals(title, actual.title)
    assertEquals(parameterValue1, TestUtil.getJsonBody(actual.body, parameterKey1))
    assertEquals(parameterValue2, TestUtil.getJsonBody(actual.body, parameterKey2))
    assertEquals(Constant.HttpMethod.DELETE.name, TestUtil.getMethodBody(actual.body))
  }

  @Test
  fun executeDeleteHeaderTest() {
    val title = "test_delete_header_anything"
    val headerKey1 = "Header-Key1"
    val headerValue1 = "HeaderValue1"
    val headerKey2 = "Header-Key2"
    val headerValue2 = "HeaderValue2"

    val actual = runBlocking {
      requester.execute(RequestParams(
        title,
        "https://httpbin.org/anything",
        Constant.HttpMethod.DELETE,
        Constant.BodyType.FORM_PARAMS,
        mapOf(
          headerKey1 to headerValue1,
          headerKey2 to headerValue2,
        ),
        null
      ))
    }
    assertEquals(200, actual.responseCode)
    assertEquals(title, actual.title)
    assertEquals(headerValue1, TestUtil.getHeadersBody(actual.body, headerKey1))
    assertEquals(headerValue2, TestUtil.getHeadersBody(actual.body, headerKey2))
    assertEquals(Constant.HttpMethod.DELETE.name, TestUtil.getMethodBody(actual.body))
  }

  @Test
  fun executeDeleteHeaderAndFormTest() {
    val title = "test_delete_header_and_form_anything"
    val headerKey1 = "Header-Key1"
    val headerValue1 = "HeaderValue1"
    val headerKey2 = "Header-Key2"
    val headerValue2 = "HeaderValue2"
    val parameterKey1 = "parameterKey1"
    val parameterValue1 = "parameterValue1"
    val parameterKey2 = "parameterKey2"
    val parameterValue2 = "parameterValue2"

    val actual = runBlocking {
      requester.execute(RequestParams(
        title,
        "https://httpbin.org/anything",
        Constant.HttpMethod.DELETE,
        Constant.BodyType.FORM_PARAMS,
        mapOf(
          headerKey1 to headerValue1,
          headerKey2 to headerValue2,
        ),
        mapOf(
          parameterKey1 to parameterValue1,
          parameterKey2 to parameterValue2,
        )
      ))
    }
    assertEquals(200, actual.responseCode)
    assertEquals(title, actual.title)
    assertEquals(headerValue1, TestUtil.getHeadersBody(actual.body, headerKey1))
    assertEquals(headerValue2, TestUtil.getHeadersBody(actual.body, headerKey2))
    assertEquals(parameterValue1, TestUtil.getFormBody(actual.body, parameterKey1))
    assertEquals(parameterValue2, TestUtil.getFormBody(actual.body, parameterKey2))
    assertEquals(Constant.HttpMethod.DELETE.name, TestUtil.getMethodBody(actual.body))
  }


  @Test
  fun executeDeleteHeaderAndJsonTest() {
    val title = "test_delete_header_and_form_anything"
    val headerKey1 = "Header-Key1"
    val headerValue1 = "HeaderValue1"
    val headerKey2 = "Header-Key2"
    val headerValue2 = "HeaderValue2"
    val parameterKey1 = "parameterKey1"
    val parameterValue1 = "parameterValue1"
    val parameterKey2 = "parameterKey2"
    val parameterValue2 = "parameterValue2"

    val actual = runBlocking {
      requester.execute(RequestParams(
        title,
        "https://httpbin.org/anything",
        Constant.HttpMethod.DELETE,
        Constant.BodyType.JSON,
        mapOf(
          headerKey1 to headerValue1,
          headerKey2 to headerValue2,
        ),
        mapOf(
          parameterKey1 to parameterValue1,
          parameterKey2 to parameterValue2,
        )
      ))
    }
    assertEquals(200, actual.responseCode)
    assertEquals(title, actual.title)
    assertEquals(headerValue1, TestUtil.getHeadersBody(actual.body, headerKey1))
    assertEquals(headerValue2, TestUtil.getHeadersBody(actual.body, headerKey2))
    assertEquals(parameterValue1, TestUtil.getJsonBody(actual.body, parameterKey1))
    assertEquals(parameterValue2, TestUtil.getJsonBody(actual.body, parameterKey2))
    assertEquals(Constant.HttpMethod.DELETE.name, TestUtil.getMethodBody(actual.body))
  }

  @Test
  fun executeDeleteStatusCodeTest() {
    val title = "test_delete_status_code"
    val statuses = listOf(
      200,
      201,
      400,
      401,
      403,
      404,
      500,
      503
    )

    statuses.forEach {
      val actual = runBlocking {
        requester.execute(RequestParams(
          title,
          "https://httpbin.org/status/$it",
          Constant.HttpMethod.DELETE,
          Constant.BodyType.FORM_PARAMS,
          null,
          null
        ))
      }
      assertEquals(it, actual.responseCode)
      assertEquals(title, actual.title)
    }
  }
}