#!/usr/bin/python
import time
from daemon import runner
from macpath import splitext
import pika, json
import sys, os

path = os.environ.get('CALIBRE_PYTHON_PATH', '/usr/lib/calibre')
if path not in sys.path:
    sys.path.insert(0, path)

sys.resources_location = os.environ.get('CALIBRE_RESOURCES_PATH', '/usr/share/calibre')
sys.extensions_location = os.environ.get('CALIBRE_EXTENSIONS_PATH', '/usr/lib/calibre/calibre/plugins')
sys.executables_location = os.environ.get('CALIBRE_EXECUTABLES_PATH', '/usr/bin')


from calibre.ebooks.conversion.cli import main



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
            method_frame, header_frame, body = ch.basic_get(queue = "pisets.tasks")
            if method_frame:
                message = json.loads(body)
                path = message[u'path']
                print message
                main(['/usr/bin/ebook-convert', path, splitext(path)[0] + "." + message[u'to']])
                ch.basic_ack(method_frame.delivery_tag)
            else:
                print("No message returned")
                time.sleep(10)

app = App()
daemon_runner = runner.DaemonRunner(app)
daemon_runner.do_action()
