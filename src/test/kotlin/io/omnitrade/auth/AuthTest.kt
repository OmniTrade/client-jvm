package io.omnitrade.auth

import com.winterbe.expekt.expect
import io.omnitrade.enumerator.http.HttpMethod
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AuthTest : Spek({
  describe("Auth") {

    val kodeinTest = Kodein {
      bind(tag = "currentTime") from provider {
        "123"
      }
    }

    describe("#signedParams") {
      context("when getting the signed params") {

        it("always returns the same signed params for the given entries") {
          val subject = Auth("accessKey", "secretKey", kodeinTest)
          val verb = HttpMethod.GET.toString()
          val path = "api/v2/path"
          val params = mutableMapOf("param1" to "value1")
          val result = "efc54fbaa06f4e09502738b67543f66c6cf8d435fe13c3a0c933c581d848314e"
          expect(subject.signedParams(verb, path, params)).to.be.equal(result)
        }
      }
    }

    describe("#hasKeys") {
      context("when checking instance with keys") {
        it("returns true") {
          val subject = Auth("accessKey", "secretKey")
          expect(subject.hasKeys()).to.be.`true`
        }
      }

      context("when checking instance without keys") {
        it("returns false") {
          val subject = Auth()
          expect(subject.hasKeys()).to.be.`false`
        }
      }
    }
  }
})
