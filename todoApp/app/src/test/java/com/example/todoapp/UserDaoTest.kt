// UserDaoTest.kt
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testRegisterAndLoginUser() = runBlocking {
        val user = User(username = "testuser", password = "password123")
        userDao.registerUser(user)
        val retrievedUser = userDao.loginUser("testuser", "password123")
        assertNotNull(retrievedUser)
        assertEquals(user.username, retrievedUser?.username)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateUser() = runBlocking {
        val user = User(username = "testuser", password = "password123")
        userDao.registerUser(user)
        val registeredUser = userDao.loginUser("testuser", "password123")
        registeredUser?.let {
            val updatedUser = it.copy(password = "newpassword")
            userDao.updateUser(updatedUser)
            val retrievedUser = userDao.loginUser("testuser", "newpassword")
            assertNotNull(retrievedUser)
            assertEquals(updatedUser.password, retrievedUser?.password)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteUser() = runBlocking {
        val user = User(username = "testuser", password = "password123")
        userDao.registerUser(user)
        val registeredUser = userDao.loginUser("testuser", "password123")
        registeredUser?.let {
            userDao.deleteUser(it.userId)
            val retrievedUser = userDao.loginUser("testuser", "password123")
            assertNull(retrievedUser)
        }
    }
}
