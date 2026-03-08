package toby.spring.user.service

import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

class DummyMailSender: MailSender {
    override fun send(p0: SimpleMailMessage?) {
    }

    override fun send(p0: Array<out SimpleMailMessage?>?) {
    }
}