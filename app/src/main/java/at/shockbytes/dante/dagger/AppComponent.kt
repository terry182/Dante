package at.shockbytes.dante.dagger

import at.shockbytes.dante.ui.activity.BackupActivity
import at.shockbytes.dante.ui.activity.BookRetrievalActivity
import at.shockbytes.dante.ui.activity.DetailActivity
import at.shockbytes.dante.ui.activity.MainActivity
import at.shockbytes.dante.ui.activity.SearchActivity
import at.shockbytes.dante.ui.fragment.BackupFragment
import at.shockbytes.dante.ui.fragment.BookDetailFragment
import at.shockbytes.dante.ui.fragment.DownloadBookFragment
import at.shockbytes.dante.ui.fragment.MainBookFragment
import at.shockbytes.dante.ui.fragment.ManualAddFragment
import at.shockbytes.dante.ui.fragment.MenuFragment
import at.shockbytes.dante.ui.fragment.QueryCaptureFragment
import at.shockbytes.dante.ui.fragment.SearchFragment
import at.shockbytes.dante.ui.fragment.SettingsFragment
import at.shockbytes.dante.ui.fragment.StatisticsFragment
import at.shockbytes.dante.ui.fragment.SuggestionsFragment
import at.shockbytes.dante.ui.fragment.dialog.GoogleSignInDialogFragment
import at.shockbytes.dante.ui.fragment.dialog.GoogleWelcomeScreenDialogFragment
import at.shockbytes.dante.ui.fragment.dialog.NotesDialogFragment
import at.shockbytes.dante.ui.fragment.dialog.RateBookDialogFragment
import at.shockbytes.dante.ui.fragment.dialog.SortStrategyDialogFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Author:  Martin Macheiner
 * Date:    19.01.2017
 */
@Singleton
@Component(modules = [
    (NetworkModule::class),
    (AppModule::class),
    (BookModule::class),
    (ViewModelModule::class),
    (FirebaseModule::class)
])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: DetailActivity)

    fun inject(activity: BookRetrievalActivity)

    fun inject(activity: SearchActivity)

    fun inject(activity: BackupActivity)

    fun inject(fragment: MainBookFragment)

    fun inject(fragment: DownloadBookFragment)

    fun inject(fragment: BackupFragment)

    fun inject(fragment: QueryCaptureFragment)

    fun inject(fragment: SearchFragment)

    fun inject(fragment: SuggestionsFragment)

    fun inject(fragment: BookDetailFragment)

    fun inject(fragment: MenuFragment)

    fun inject(fragment: ManualAddFragment)

    fun inject(fragment: StatisticsFragment)

    fun inject(fragment: SettingsFragment)

    fun inject(dialogFragment: GoogleSignInDialogFragment)

    fun inject(dialogFragment: SortStrategyDialogFragment)

    fun inject(dialogFragment: NotesDialogFragment)

    fun inject(dialogFragment: RateBookDialogFragment)

    fun inject(dialogFragment: GoogleWelcomeScreenDialogFragment)
}
