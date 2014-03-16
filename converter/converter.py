#!/usr/bin/python
import time
from daemon import runner
import pika


class App():
    def __init__(self):
        self.stdin_path = '/dev/null'
        self.stdout_path = '/dev/tty'
        self.stderr_path = '/dev/tty'
        self.pidfile_path =  '/tmp/foo.pid'
        self.pidfile_timeout = 5
    def run(self):
        while True:
            conn = pika.BlockingConnection()
            ch =  conn.channel()
            method_frame, header_frame, body = ch.basic_get(queue = "pisets.tasks", no_ack = True)
            if method_frame:
                print method_frame, header_frame, body
                ch.basic_ack(method_frame.delivery_tag)
            else:
                print("No message returned")
                time.sleep(10)

app = App()
daemon_runner = runner.DaemonRunner(app)
daemon_runner.do_action()
