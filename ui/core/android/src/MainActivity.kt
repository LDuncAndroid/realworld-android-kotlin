package realworld.ui.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.erased.factory
import realworld.ui.navigation.LaunchScreen

/**
 * As the only activity in the app, here we configure
 * Conductor and drive the app's [LaunchScreen].
 */
class MainActivity : AppCompatActivity(), KodeinAware {

  /** The [Router] managed by this activity. */
  private lateinit var router: Router

  /** The [LaunchScreen] used for cold app start. */
  private val launchScreen: (Router) -> LaunchScreen by factory()

  override val kodein by closestKodein()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(ChangeHandlerFrameLayout(this).also { view ->
      router = Conductor.attachRouter(this, view, savedInstanceState)
    })
    if (!router.hasRootController()) {
      launchScreen(router).launch()
    }
  }

  /**
   * Delegate back press handling to [router] until
   * the last controller is removed.
   */
  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
    }
  }
}