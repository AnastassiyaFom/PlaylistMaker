package com.practicum.playlistmaker.settings.domain

class SharingInteractorImpl (
    private val sharing: SharingRepository,
    private val externalNavigator:ExternalNavigator
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.share(sharing.getShareMessage())
    }

    override fun openTerms() {
        externalNavigator.openLink(sharing.getUserAgreementLink())
    }

    override fun openSupport() {
        externalNavigator.sendEmail(sharing.getSupportEmailData())
    }
}
