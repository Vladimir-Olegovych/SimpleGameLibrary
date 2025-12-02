package tools.graphics.screens.navigation

import com.badlogic.gdx.Game
import tools.graphics.screens.fragment.Fragment

typealias NavComponent<D> = (D) -> Fragment

interface NavigationListener<D: Any> {
    fun onNavigationListener(currentDestination: D?, nextDestination: D): Boolean = true
    fun onNavigationSuccess(destination: D, fragment: Fragment) {}
}

class NavHostController<D: Any>(private val game: Game) {

    val navComponentMap = HashMap<Class<out D>, NavComponent<D>>()
    private var navigationListener: NavigationListener<D>? = null
    private var currentFragment: Fragment? = null
    private var currentDestination: D? = null

    fun setOnNavigationListener(navigationListener: NavigationListener<D>){
        this.navigationListener = navigationListener
    }

    inline fun <reified C : D> fragment(noinline navComponent: NavComponent<C>) {
        navComponentMap[C::class.java] = { destination ->
            navComponent(destination as C)
        }
    }

    fun navigate(destination: D) {
        val navComponent = navComponentMap[destination::class.java] ?: return
        if (currentDestination == destination) return
        val navigateListenerResult = navigationListener?.onNavigationListener(currentDestination, destination)
        if (navigateListenerResult == false) return

        try {
            val fragment = navComponent(destination)
            navigationListener?.onNavigationSuccess(destination, fragment)
            currentFragment?.destroyFragment()
            currentFragment = fragment
            fragment.onCreate()
            game.screen = fragment.screen
        } catch (e: Throwable) {
            game.screen = null
            currentFragment = null
            e.printStackTrace()
        }
    }
}
