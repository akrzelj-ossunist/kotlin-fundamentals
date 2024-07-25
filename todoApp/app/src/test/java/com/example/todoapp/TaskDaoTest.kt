// TaskDaoTest.kt
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
class TaskDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        taskDao = db.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testCreateAndRetrieveTask() = runBlocking {
        val task = Task(userId = 1, description = "Test Task", priority = 1)
        taskDao.createTask(task)
        val tasks = taskDao.getTasksByUserId(1)
        assertEquals(1, tasks.size)
        assertEquals(task.description, tasks[0].description)
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteTask() = runBlocking {
        val task = Task(userId = 1, description = "Test Task", priority = 1)
        taskDao.createTask(task)
        val tasks = taskDao.getTasksByUserId(1)
        val taskId = tasks[0].taskId
        taskDao.deleteTask(taskId)
        val updatedTasks = taskDao.getTasksByUserId(1)
        assertTrue(updatedTasks.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testFilterTasks() = runBlocking {
        val task1 = Task(userId = 1, description = "Task 1", priority = 1, isCompleted = false)
        val task2 = Task(userId = 1, description = "Task 2", priority = 2, isCompleted = true)
        taskDao.createTask(task1)
        taskDao.createTask(task2)

        val highPriorityTasks = taskDao.filterTasks(1, 1, false)
        assertEquals(1, highPriorityTasks.size)
        assertEquals(task1.description, highPriorityTasks[0].description)

        val completedTasks = taskDao.filterTasks(1, 2, true)
        assertEquals(1, completedTasks.size)
        assertEquals(task2.description, completedTasks[0].description)
    }
}
