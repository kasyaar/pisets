import smtpd
import asyncore
from email.parser import Parser

class CustomSMTPServer(smtpd.SMTPServer):
    
    def process_message(self, peer, mailfrom, rcpttos, data):
        # print data
        res = Parser().parsestr(data)
        print type(res)
        print res.keys()
        # print res.get_payload()[0]
        # print "\n\n\n"
        print res.get_payload()[1].get_payload(decode=True)
        # print res
        # print 'Receiving message from:', peer
        # print 'Message addressed from:', mailfrom
        # print 'Message addressed to  :', rcpttos
        # print 'Message length        :', len(data)
        return

server = CustomSMTPServer(('127.0.0.1', 1025), None)

asyncore.loop()
