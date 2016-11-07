# GS16
Projekt  VK.com


# Format of output json file:

## For 'postLists' with Keyword search
### The json contains 3 json Arrays:
1. Groups for information about the owned and linked groups with the following keys:
  -gid: id of the group
  -is_closed: boolean value, 0 if the group is active
  -name: headline name of the group
  -photo: small profile foto of the group
  -photo_big: profile foto of the group
  -screen_name: url name of the group
2. profile information about the post owners in post list:
  -first_name
  -last_name
  -photo: small profile picture of the person
  -photo_medium_rec: profile picture of the person
  -screen_name: screen name of the person
  -sex: gender
  -uid: user id of the person
3. The wall array with a list of posts similar to the 'postLists' without Keyword search
#####This format information just contains the most important tags.
#####For additional information visit: 
-[The official vk.com post group object documentation](https://vk.com/dev/fields_groups)
-[The official vk.com user field documentation](https://vk.com/dev/fields)

## For 'postLists' without Keyword search
### The json contains an array of post objects with the following keys:
-date: unix date of post creation
-from_id: the post owners id
-id: the posts id
-likes: number of likes the post got
-reposts: number of repostings
-text: the text of the post
-to_id: id of the group or person who owns the wall

Additional to those keys, the post-object could contain Attachements e.g. Photos, Videos.
Those attachements contain additional source data of the media:

###### For foto media:
-src: small preview picture of the shared foto
-src_big: the shared foto
###### For video media:
-duration: duration of the video in seconds
-image: small preview picture for the video
-image_big: preview picture for the video
-platform: platform e.g. youtube
-title: title of the video
#####This format information just contains the most important tags.
#####For additional information visit: 
-[The official vk.com post object documentation](https://vk.com/dev/post)
-[The official vk.com attachement field documentation](https://vk.com/dev/attachments_w)

