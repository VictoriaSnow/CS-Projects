import urlparse
import urllib
from evernote.api.client import EvernoteClient

import evernote.edam.userstore.UserStore as UserStore
import evernote.edam.notestore.NoteStore as NoteStore
import thrift.protocol.TBinaryProtocol as TBinaryProtocol
import thrift.transport.THttpClient as THttpClient


import oauth2 as oauth
from flask import Flask, session, redirect, url_for, request, render_template


app = Flask(__name__)

APP_SECRET_KEY = \
    'YOUR KEY HERE'

EN_CONSUMER_KEY = ' sepkristina'
EN_CONSUMER_SECRET = '0bdfd269114353a2'

EN_REQUEST_TOKEN_URL = 'https://sandbox.evernote.com/oauth'
EN_ACCESS_TOKEN_URL = 'https://sandbox.evernote.com/oauth'
EN_AUTHORIZE_URL = 'https://sandbox.evernote.com/OAuth.action'

EN_HOST = "sandbox.evernote.com"
EN_USERSTORE_URIBASE = "https://" + EN_HOST + "/edam/user"
EN_NOTESTORE_URIBASE = "https://" + EN_HOST + "/edam/note/"


def get_oauth_client(token=None):
    """Return an instance of the OAuth client."""
    consumer = oauth.Consumer(EN_CONSUMER_KEY, EN_CONSUMER_SECRET)
    if token:
        client = oauth.Client(consumer, token)
    else:
        client = oauth.Client(consumer)
    return client


def get_notestore():
    """Return an instance of the Evernote NoteStore. Assumes that 'shardId' is
    stored in the current session."""
    shardId = session['shardId']
    noteStoreUri = EN_NOTESTORE_URIBASE + shardId
    noteStoreHttpClient = THttpClient.THttpClient(noteStoreUri)
    noteStoreProtocol = TBinaryProtocol.TBinaryProtocol(noteStoreHttpClient)
    noteStore = NoteStore.Client(noteStoreProtocol)
    return noteStore


def get_userstore():
    """Return an instance of the Evernote UserStore."""
    userStoreHttpClient = THttpClient.THttpClient(EN_USERSTORE_URIBASE)
    userStoreProtocol = TBinaryProtocol.TBinaryProtocol(userStoreHttpClient)
    userStore = UserStore.Client(userStoreProtocol)
    return userStore


@app.route('/')
def index():
    #index_str = '<p><a href="/auth">Authorize this application</a></p>'
    #index_str += '<p><a href="/notebook">View a notebook name</a></p>'
    #return index_str
    return render_template('index.html')


@app.route('/auth')
def auth_start():
    dev_token = "S=s1:U=8f624:E=14fa3a78539:C=1484bf65560:P=1cd:A=en-devtoken:V=2:H=a71244ac77727e1a6a2fcb5286ab435a"
    client = EvernoteClient(token=dev_token)
    userStore = client.get_user_store()
    user = userStore.getUser()
    list = [1, 2, 3, 4]
    return render_template('test.html', list = list)

@app.route('/authComplete')
def auth_finish():
    """After the user has authorized this application on Evernote's website,
    they will be redirected back to this URL to finish the process."""

    oauth_verifier = request.args.get('oauth_verifier', '')

    token = oauth.Token(session['oauth_token'], session['oauth_token_secret'])
    token.set_verifier(oauth_verifier)

    client = get_oauth_client()
    client = get_oauth_client(token)

    # Retrieve the token credentials (Access Token) from Evernote
    resp, content = client.request(EN_ACCESS_TOKEN_URL, 'POST')

    if resp['status'] != '200':
        raise Exception('Invalid response %s.' % resp['status'])

    access_token = dict(urlparse.parse_qsl(content))
    authToken = access_token['oauth_token']

    userStore = get_userstore()
    user = userStore.getUser(authToken)

    # Save the users information to so we can make requests later
    session['shardId'] = user.shardId
    session['identifier'] = authToken

    return "<ul><li>oauth_token = %s</li><li>shardId = %s</li></ul>" % (
        authToken, user.shardId)


@app.route('/notebook')
def default_notbook():
    authToken = session['identifier']
    noteStore = get_notestore()
    notebooks = noteStore.listNotebooks(authToken)
    for notebook in notebooks:
            defaultNotebook = notebook
            break

    return defaultNotebook.name


if __name__ == '__main__':
    #app.secret_key = APP_SECRET_KEY
    app.run(debug=True)