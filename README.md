# GS16 - post collection on VK.com
## How to use:

1. Download The Repo [GS16_compiled](https://github.com/Eik-S/GS16_compiled)
2. Execute GS16.jar in terminal using the following Arguments:
  - updateData: Get all Posts from the groups/persons wall set in 'vk_id_list.txt' or update the already collected data for new posts 
    - The data will be saved in the data Folder, containing also a 'groupInfo.json' :exclamation: dont touch :exclamation:
    - Posts will be saved in files per execution day. Please use this method just once on the initial day because of huge files been written.
  - wallSearch: Search for All Keywords from 'vk_keyword_list.txt' in all groups/persons set in 'vk_id_list.txt'
  - createGroupInfo: Create a Table with Domain, Name and Url information to every id in 'vk_id_list.txt'
  - findIds: Add the ids of the groups in 'vk_domain_list.txt' to 'vk_id_list.txt'
    - the domain list must contain the name part of the groups url if the groups url is not defined by its id, if the url e.g. is 'vk.com/group/patrioten', the list should contain 'patrioten'
  
### Examples:
```
java -jar GS16.jar wallGet
java -jar GS16.jar wallSearch
...
```

## Important:
The text lists are located at src/inputLists/fooBar.txt
'wallSearch' will create a new Folder named with the current date/time
#### :exclamation: Actually its not possible to work with own filenames or directorys :exclamation: 


# Format of output json file:

## For 'postLists' with Keyword search
### The json contains 3 json Arrays:
1. Groups for information about the owned and linked groups with the following keys:
  - gid: id of the group
  - is_closed: boolean value, 0 if the group is active
  - name: headline name of the group
  - photo: small profile foto of the group
  - photo_big: profile foto of the group
  - screen_name: url name of the group
2. profile information about the post owners in post list:
  - first_name
  - last_name
  - photo: small profile picture of the person
  - photo_medium_rec: profile picture of the person
  - screen_name: screen name of the person
  - sex: gender
  - uid: user id of the person
3. The wall array with a list of posts similar to the 'postLists' without Keyword search

##### This format information just contains the most important tags.
##### For additional information visit: 
- [The official vk.com post group object documentation](https://vk.com/dev/fields_groups)
- [The official vk.com user field documentation](https://vk.com/dev/fields)

## For 'postLists' without Keyword search
### The json contains an array of post objects with the following keys:
- date: unix date of post creation
- from_id: the post owners id
- id: the posts id
- likes: number of likes the post got
- reposts: number of repostings
- text: the text of the post
- to_id: id of the group or person who owns the wall

Additional to those keys, the post-object could contain Attachements e.g. Photos, Videos.
Those attachements contain additional source data of the media:

###### For foto media:
- src: small preview picture of the shared foto
- src_big: the shared foto

###### For video media:
- duration: duration of the video in seconds
- image: small preview picture for the video
- image_big: preview picture for the video
- platform: platform e.g. youtube
- title: title of the video

##### This format information just contains the most important tags.
##### For additional information visit: 
- [The official vk.com post object documentation](https://vk.com/dev/post)
- [The official vk.com attachement field documentation](https://vk.com/dev/attachments_w)

