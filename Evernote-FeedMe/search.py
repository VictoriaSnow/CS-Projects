# Python backend stuff

from evernote.api.client import EvernoteClient
from evernote.edam.notestore.ttypes import NoteFilter, NotesMetadataResultSpec
from evernote.edam.type.ttypes import NoteSortOrder
from evernote.edam.type.ttypes import Note
import evernote.edam.userstore.constants as UserStoreConstants



# auth_token = "S=s1:U=8f624:E=14fa3a78539:C=1484bf65560:P=1cd:A=en-devtoken:V=2:H=a71244ac77727e1a6a2fcb5286ab435a"
# client = EvernoteClient(token=auth_token)
# note_store = client.get_note_store()

# updated_filter = NoteFilter(order=NoteSortOrder.UPDATED,words="chicken")
# offset = 0
# max_notes = 10
# result_spec = NotesMetadataResultSpec(includeTitle=True)
# result_list = note_store.findNotesMetadata(auth_token, updated_filter, offset, max_notes, result_spec)

# # note is an instance of NoteMetadata
# # result_list is an instance of NotesMetadataList
# for note in result_list.notes:
#     print note.title


# takes a list of strings (search paramaters) and user's auth token
# returns a list of results (as note objects) sorted from most to least relevant
def frontendSearch(listOfStrings, userToken):
	
	# change auth_token to user token eventually
	auth_token = "S=s1:U=8f624:E=14fa3a78539:C=1484bf65560:P=1cd:A=en-devtoken:V=2:H=a71244ac77727e1a6a2fcb5286ab435a"
	client = EvernoteClient(token=auth_token) # change to userToken eventually
	note_store = client.get_note_store()
	updated_filter = NoteFilter(order=NoteSortOrder. UPDATED, words="")
	offset = 0
	max_notes = 999
	
	result_spec = NotesMetadataResultSpec(includeTitle=True)

	for string in listOfStrings:
		updated_filter.words += string + " "

	result_list = note_store.findNotesMetadata(auth_token, updated_filter, offset, max_notes, result_spec).notes

	countDict = {}
	for note in result_list:
		countDict[note] = 1
		for string in listOfStrings:
			noteText = note_store.getNoteSearchText(auth_token, note.guid, 0, 1)
			countDict[note] *= noteText.count(string)



	
	result_list.sort(key=countDict.get, reverse=True)


	counter = 0
	for note in frontendSearch(["chicken"], "null"):
		print(note.title, countDict[note])
		if counter > 10:
			break
		else:
			counter += 1


	return result_list

# enumerates mime types frontend wants to handle
usable_mimes = ["image/gif", "image/jpeg", "image/png"]

# fetches an arbitrary resource of desired mime type from given node
# returns a tuple with the MIME and Data object of found Resource upon success
# returns ("fail to find", null) upon failure to find

def getPicture(note, userToken):
	auth_token = "S=s1:U=8f624:E=14fa3a78539:C=1484bf65560:P=1cd:A=en-devtoken:V=2:H=a71244ac77727e1a6a2fcb5286ab435a"
	client = EvernoteClient(token=auth_token)
	note_store = client.get_note_store()
	noteData = NoteStore.getNote(auth_token, note.guid, False, False, False)
	for resource in noteData.resources:
		newResource = NoteStore.getResource(auth_token, resource.guid, True, False, False, False)
		if newResource.mime in usable_mimes:
			return (newResource.mime, newResource.data)

	return ("fail to find", null)

# returns a list of the name of each user notebook
def listOfUserNotebooks(userToken):
	auth_token = "S=s1:U=8f624:E=14fa3a78539:C=1484bf65560:P=1cd:A=en-devtoken:V=2:H=a71244ac77727e1a6a2fcb5286ab435a"
	client = EvernoteClient(token=auth_Token)
	note_store = client.get_note_store()
	notebookList = note_store.listNotebooks(auth_token)

	returnList = []
	for notebook in notebookList:
		returnList.append(notebook.name)
	return returnList


