from evernote.api.client import EvernoteClient

dev_token = "S=s1:U=8f624:E=14fa3a78539:C=1484bf65560:P=1cd:A=en-devtoken:V=2:H=a71244ac77727e1a6a2fcb5286ab435a"
client = EvernoteClient(token=dev_token)
userStore = client.get_user_store()
user = userStore.getUser()
print user.username
