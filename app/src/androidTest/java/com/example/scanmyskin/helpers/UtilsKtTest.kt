package com.example.scanmyskin.helpers

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilsKtTest {

    @Test
    fun validEmailAndCorrectlyRepeatedPasswordReturnsTrue() {
        assertThat(validateRegistrationInput("lukasutalo@protonmail.com","Lukenda1!!","Lukenda1!!")).isTrue()
    }

    @Test
    fun validEmailAndIncorrectlyRepeatedPasswordReturnsFalse() {
        assertThat(validateRegistrationInput("lukasutalo@protonmail.com","Lukenda1!!","Lukenda122!!")).isFalse()
    }

    @Test
    fun passwordEmptyReturnsFalse() {
        assertThat(validateRegistrationInput("lukasutalo@protonmail.com","","")).isFalse()
    }

    @Test
    fun passwordDoesNotMatchConfirmedPasswordReturnsFalse() {
        assertThat(validateRegistrationInput("lukasutalo@protonmail.com","Lukenda1!!","Luken1231sdada1!!")).isFalse()
    }

    @Test
    fun passwordContainsLessThanEightCharactersReturnsFalse() {
        assertThat(validateRegistrationInput("lukasutalo@protonmail.com","Luk2!","Luk2!")).isFalse()
    }

    @Test
    fun passwordDoesNotContainSpecialCharacterReturnsFalse() {
        assertThat(validateRegistrationInput("lukasutalo@protonmail.com","Lukenda1231","Lukenda1231")).isFalse()
    }

    @Test
    fun passwordDoesNotContainUppercaseLetterReturnsFalse() {
        assertThat(validateRegistrationInput("lukasutalo@protonmail.com","lukenda1231!!","lukenda1231!!")).isFalse()
    }

    @Test
    fun emailValidReturnsTrue() {
        try {
            isValidEmail( "emmanuel@hibernate.org")
            isValidEmail( "emmanuel@hibernate")
            isValidEmail( "emma-n_uel@hibernate")
            isValidEmail( "emma+nuel@hibernate.org")
            isValidEmail( "emma=nuel@hibernate.org")
            isValidEmail( "emmanuel@[123.12.2.11]")
            isValidEmail( "*@example.net")
            isValidEmail( "fred&barny@example.com")
            isValidEmail( "---@example.com")
            isValidEmail( "foo-bar@example.net")
            isValidEmail( "mailbox.sub1.sub2@this-domain")
            isValidEmail( "prettyandsimple@example.com")
            isValidEmail( "very.common@example.com")
            isValidEmail( "disposable.style.email.with+symbol@example.com")
            isValidEmail( "other.email-with-dash@example.com")
            isValidEmail( "x@example.com")
            isValidEmail( "\"much.more unusual\"@example.com")
            isValidEmail( "\"very.unusual.@.unusual.com\"@example.com")
            isValidEmail( "\"very.(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com")
            isValidEmail( "\"some \".\" strange \".\" part*:; \"@strange.example.com")
            isValidEmail( "example-indeed@strange-example.com")
            isValidEmail( "admin@mailserver1")
            isValidEmail( "#!$%&'*+-/=?^_`{}|~@example.org")
            isValidEmail( "\"()<>[]:,;@\\\"!#$%&'-/=?^_`{}| ~.a\"@example.org")
            isValidEmail( "\" \"@example.org")
            isValidEmail( "example@localhost")
            isValidEmail( "example@s.solutions")
            isValidEmail( "user@localserver")
            isValidEmail( "user@tt")
            isValidEmail( "user@[IPv6:2001:DB8::1]")
            isValidEmail( "xn--80ahgue5b@xn--p-8sbkgc5ag7bhce.xn--ba-lmcq")
            isValidEmail( "nothing@xn--fken-gra.no")
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    @Test
    fun emailInvalidReturnsFalse(){
        isInvalidEmail( "emmanuel.hibernate.org")
        isInvalidEmail( "emma nuel@hibernate.org")
        isInvalidEmail( "emma(nuel@hibernate.org")
        isInvalidEmail( "emmanuel@")
        isInvalidEmail( "emma\nnuel@hibernate.org")
        isInvalidEmail( "emma@nuel@hibernate.org")
        isInvalidEmail( "emma@nuel@.hibernate.org")
        isInvalidEmail( "Just a string")
        isInvalidEmail( "string")
        isInvalidEmail( "me@")
        isInvalidEmail( "@example.com")
        isInvalidEmail( "me.@example.com")
        isInvalidEmail( ".me@example.com")
        isInvalidEmail( "me@example..com")
        isInvalidEmail( "me\\@example.com")
        isInvalidEmail( "Abc.example.com") // (no @ character)
        isInvalidEmail( "A@b@c@example.com") // (only one @ is allowed outside quotation marks)
        isInvalidEmail( "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com") // (none of the special characters in this local-part are allowed outside quotation marks)
        isInvalidEmail( "just\"not\"right@example.com") // (quoted strings must be dot separated or the only element making up the local-part)
        isInvalidEmail( "this is\"not\\allowed@example.com") // (spaces, quotes, and backslashes may only exist when within quoted strings and preceded by a backslash)
        isInvalidEmail( "this\\ still\\\"not\\\\allowed@example.com") // (even if escaped (preceded by a backslash), spaces, quotes, and backslashes must still be contained by quotes)
        isInvalidEmail( "john..doe@example.com") // (double dot before @) with caveat: Gmail lets this through, Email address#Local-part the dots altogether
        isInvalidEmail( "john.doe@example..com")
    }

    private fun isValidEmail(email: String) {
        assertThat(email.isEmailValid()).isTrue()
    }

    private fun isInvalidEmail(email: String) {
        assertThat(email.isEmailValid()).isFalse()
    }
}