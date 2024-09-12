import android.content.Context
import android.os.Build
import com.catshouse.finance.model.Repositorio
import com.catshouse.finance.viewmodel.MainActivityViewMoel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class MainActivityViewMoelTest {

    @Mock
    lateinit var repositorio: Repositorio

    lateinit var viewModel: MainActivityViewMoel

    lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        Firebase.initialize(context)
        MockitoAnnotations.initMocks(this)
        viewModel = MainActivityViewMoel(repositorio)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `test verificarUsuarioLogado success`() = runBlocking {
        // Configurar o comportamento do Repositorio mockado
        `when`(repositorio.verificarUsuarioLogado()).thenReturn(mock(FirebaseUser::class.java))

        // Chamar a função a ser testada
        val result = viewModel.verificarUsuarioLogado()

        // Verificar o resultado
        assertNotNull(result)
    }
}
