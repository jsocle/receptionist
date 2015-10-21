import com.github.jsocle.receptionist.app
import com.github.jsocle.receptionist.blueprints.loginApp
import com.github.jsocle.receptionist.blueprints.mainApp
import com.github.jsocle.receptionist.models.User
import com.github.jsocle.requests.Request
import org.junit.Assert
import org.junit.Test

class AppTest {
    @Test
    fun testLoginRequire() {
        app.db.session {
            it.persist(User(userId = "john", password = "john"))
            it.flush()
        }

        val client = app.client
        // test login controller is not forwarded.
        Assert.assertEquals(loginApp.login.url(), client.get(loginApp.login.url()).url)
        // test if user is not login, then forward to login page.
        Assert.assertEquals(loginApp.login.url(), client.get(mainApp.index.url()).url)

        // after user login, then show requested page.
        client.get(loginApp.login.url("id" to "john", "password" to "john"), method = Request.Method.POST)
        Assert.assertEquals(mainApp.index.url(), client.get(mainApp.index.url()).url)
    }
}