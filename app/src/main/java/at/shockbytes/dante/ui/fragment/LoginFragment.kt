package at.shockbytes.dante.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import at.shockbytes.dante.R
import android.view.LayoutInflater
import android.view.ViewGroup
import at.shockbytes.dante.databinding.FragmentLoginBinding
import at.shockbytes.dante.injection.AppComponent
import at.shockbytes.dante.ui.activity.LoginActivity
import at.shockbytes.dante.ui.viewmodel.LoginViewModel
import at.shockbytes.dante.ui.viewmodel.MailLoginViewModel
import at.shockbytes.dante.util.MailLauncher
import at.shockbytes.dante.util.UrlLauncher
import at.shockbytes.dante.util.addTo
import at.shockbytes.dante.util.bold
import at.shockbytes.dante.util.link
import at.shockbytes.dante.util.colored
import at.shockbytes.dante.util.concat
import at.shockbytes.dante.util.setVisible
import at.shockbytes.dante.util.viewModelOfActivity
import at.shockbytes.util.AppUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        root: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, root, attachToRoot)
    }

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelOfActivity(activity as LoginActivity, vmFactory)
    }

    override fun setupViews() {

        vb.btnLoginGoogle.setOnClickListener {
            viewModel.requestGoogleLogin()
                .subscribe(::handleGoogleLoginRequest)
                .addTo(compositeDisposable)
        }

        vb.btnLoginMail.setOnClickListener {
            MailLoginBottomSheetDialogFragment
                .newInstance(MailLoginViewModel.MailLoginState.ResolveEmailAddress)
                .setOnCredentialsEnteredListener(viewModel::authorizeWithMail)
                .show(parentFragmentManager, "mail-login-fragment")
        }

        vb.btnLoginSkip.setOnClickListener {
            showAnonymousSignUpHintDialog {
                viewModel.loginAnonymously()
            }
        }

        vb.btnLoginHelp.setOnClickListener {
            viewModel.trackLoginProblemClicked()
            MailLauncher.sendMail(
                requireActivity(),
                subject = getString(R.string.login_problems_header),
                attachVersion = true
            )
        }
    }

    private fun showAnonymousSignUpHintDialog(onAcceptClicked: () -> Unit) {
        MaterialDialog(requireContext()).show {
            icon(R.drawable.ic_incognito)
            title(text = getString(R.string.login_incognito))
            message(text = getString(R.string.login_incognito_sign_up_hint))
            positiveButton(R.string.login) {
                onAcceptClicked()
                dismiss()
            }
            negativeButton(R.string.dismiss) {
                dismiss()
            }
            cancelOnTouchOutside(true)
            cornerRadius(AppUtils.convertDpInPixel(6, requireContext()).toFloat())
        }
    }

    private fun handleGoogleLoginRequest(loginIntent: Intent) {
        startForResult(loginIntent) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                viewModel.loginWithGoogle(result.data!!)
            } else {
                showSnackbar(getString(R.string.login_error_google))
            }
        }
    }

    override fun injectToGraph(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun bindViewModel() {
        viewModel.getLoginViewState().observe(this, Observer(::handleLoginViewState))
    }

    private fun handleLoginViewState(viewState: LoginViewModel.LoginViewState) {
        when (viewState) {
            LoginViewModel.LoginViewState.NewUser -> {
                setNewUserTexts()
                showTermsOfServicesButton()
            }
            LoginViewModel.LoginViewState.Standard -> {
                setStandardUserTexts()
                hideTermsOfServicesButton()
            }
        }
    }

    private fun setNewUserTexts() {
        vb.tvLoginHello.setText(R.string.welcome_to_dante)

        vb.btnLoginGoogle.setText(R.string.login_with_google)
        vb.btnLoginMail.setText(R.string.login_with_mail)
        vb.btnLoginSkip.apply {
            setText(R.string.login_anonymously)
            setIconResource(0)
        }
    }

    private fun showTermsOfServicesButton() {
        val link = getString(R.string.terms_of_services)
            .bold()
            .colored(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            .link {
                UrlLauncher.openTermsOfServicePage(requireContext())
                viewModel.trackOpenTermsOfServices()
            }

        vb.tvLoginTos.apply {
            setVisible(true)
            movementMethod = LinkMovementMethod.getInstance()
            text = getString(R.string.terms_of_services_prefix)
                .concat(link, getString(R.string.terms_of_services_suffix))
        }
    }

    private fun setStandardUserTexts() {
        vb.tvLoginHello.setText(R.string.welcome_back)

        vb.btnLoginGoogle.setText(R.string.continue_with_google)
        vb.btnLoginMail.setText(R.string.continue_with_mail)
        vb.btnLoginSkip.apply {
            setText(R.string.continue_anonymously)
            setIconResource(R.drawable.ic_incognito)
        }
    }

    private fun hideTermsOfServicesButton() {
        vb.tvLoginTos.setVisible(false)
    }

    override fun unbindViewModel() = Unit

    companion object {

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}