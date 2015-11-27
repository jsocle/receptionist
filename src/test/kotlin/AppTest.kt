import com.github.jsocle.client.ClientResponse
import com.github.jsocle.client.TestClient
import com.github.jsocle.receptionist.app
import com.github.jsocle.receptionist.blueprints.loginApp
import com.github.jsocle.receptionist.blueprints.mainApp
import com.github.jsocle.receptionist.blueprints.reservationApp
import com.github.jsocle.receptionist.blueprints.signUpApp
import com.github.jsocle.receptionist.models.Reservation
import com.github.jsocle.receptionist.models.User
import com.github.jsocle.requests.Request
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

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

    @Test
    fun testReservationApp() {
        app.db.session {
            Assert.assertEquals(0, it.createCriteria(Reservation::class.java).list().size)
        }

        var client = login()
        // test creation
        client.addReservation("2016-01-01 09:00", "2016-01-01 10:00")

        var reservationId: Int = 0
        app.db.session {
            val reservation = it.createCriteria(Reservation::class.java).uniqueResult() as Reservation
            Assert.assertEquals(newDate(2016, 0, 1, 9, 0, 0).format(), reservation.startAt.format())
            Assert.assertEquals(newDate(2016, 0, 1, 10, 0, 0).format(), reservation.endAt.format())
            reservationId = reservation.id!!
        }

        client.get(
                reservationApp.edit.url(reservationId, "startAt" to "2016-02-01 09:00", "endAt" to "2016-02-01 10:00"),
                method = Request.Method.POST
        )
        app.db.session {
            val reservation = it.createCriteria(Reservation::class.java).uniqueResult() as Reservation
            Assert.assertEquals(newDate(2016, 1, 1, 9, 0, 0).format(), reservation.startAt.format())
            Assert.assertEquals(newDate(2016, 1, 1, 10, 0, 0).format(), reservation.endAt.format())
        }
    }

    @Test
    fun testMainApp() {
        val client = login()
        client.addReservation("2016-01-01 09:00", "2016-01-01 10:00")
        val response = client[mainApp.index.url("year" to "2016", "month" to "1")]
        Assert.assertTrue("09:00 ~ 10:00 john" in response.data)
    }

    private fun TestClient.addReservation(startAt: String, endAt: String): ClientResponse {
        return get(reservationApp.new_.url("startAt" to startAt, "endAt" to endAt), method = Request.Method.POST)
    }

    private fun login(): TestClient {
        val client = app.client
        client.get(loginApp.login.url("id" to "john", "password" to "john"), method = Request.Method.POST)
        return client
    }

    private fun newDate(year: Int, month: Int, date: Int, hourOfDay: Int, minute: Int, second: Int): Date {
        return Calendar.getInstance().let { it.set(year, month, date, hourOfDay, minute, second); it.time }
    }

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    fun Date.format(): String {
        return sdf.format(this)
    }
}