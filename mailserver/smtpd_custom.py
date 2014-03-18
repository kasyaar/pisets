import smtpd
import asyncore
from email.parser import Parser

class CustomSMTPServer(smtpd.SMTPServer):
    
    def process_message(self, peer, mailfrom, rcpttos, data):
        message = Parser().parsestr(data)
        book = message.get_payload()[1]
        print book.keys()
        f = open('/tmp/workfile.fb2', 'w') 
        f.write(book.get_payload(decode=True))
        return

server = CustomSMTPServer(('127.0.0.1', 1025), None)

asyncore.loop()
