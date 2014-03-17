import smtpd, asyncore, pika

class MailEndpoint(smtpd.SMTPServer):
    def process_message(self, peer, mailfrom, rcpttos, data):
        return

server = MailEndpoint(('127.0.0.1', 1025), None)

asyncore.loop()
