import com.github.jsocle.receptionist.app
import com.github.jsocle.receptionist.blueprints.loginApp
import com.github.jsocle.receptionist.blueprints.mainApp
import com.github.jsocle.receptionist.blueprints.signUpApp
import com.github.jsocle.receptionist.models.User
import com.github.jsocle.requests.Request
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AppTest {
    @Before
    fun before() {
        app.db.reload()
        app.db.session {
            it.persist(User(userId = "john", password = "john"))
            it.flush()
        }
    }

    @Test
    fun testLoginRequire() {
        val client = app.client
        // test login controller is not forwarded.
        Assert.assertEquals(loginApp.login.url(), client.get(loginApp.login.url()).url)
        // test if user is not login, then forward to login page.
        Assert.assertEquals(loginApp.login.url(), client.get(mainApp.index.url()).url)

        // after user login, then show requested page.
        client.get(loginApp.login.url("id" to "john", "password" to "john"), method = Request.Method.POST)
        Assert.assertEquals(mainApp.index.url(), client.get(mainApp.index.url()).url)
    }

    @Test
    fun testLoginApp() {
        Assert.assertEquals(
                mainApp.index.url(),
                app.client
                        .get(loginApp.login.url("id" to "john", "password" to "john"), method = Request.Method.POST)
                        .url
        )
    }

    @Test
    fun testSignUp() {
        app.db.session {
            Assert.assertEquals(
                    listOf<Any>(),
                    it.createQuery("from User where userId = :userId").setParameter("userId", "steve").list()
            )
        }

        Assert.assertEquals(
                mainApp.index.url(),
                app.client.get(
                        signUpApp.signUp.url("id" to "steve", "password" to "password", "passwordConfirm" to "password"),
                        method = Request.Method.POST
                ).url
        )

        app.db.session {
            Assert.assertEquals(
                    listOf("steve"),
                    it.createQuery("from User where userId = :userId")
                            .setParameter("userId", "steve")
                            .list()
                            .map { (it as User).userId }
            )
        }
    }
}