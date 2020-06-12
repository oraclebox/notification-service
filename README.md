## Notification Service

A common service send notification email by simple API.



## API

### Example: Send out plain text email with value binding

```shell
curl --request POST \
  --url http://host0001:8017/service/notification/send/email \
  --header 'content-type: application/json' \
  --data '{
	"emails":[
		{			
			"to":["roger.chan@example.com", "may.cheung@abc.com"],
			"subject":"Testing is email subject line",
			"content":{
				"template":"This is email content with value binding ($bindingkey).",
				"bindingValues": {"bindingkey":"Binding value here"}
			}
		}
	]
}
```

### Example: Send out plain text email to group of people

```shell
curl --request POST \
  --url http://host0001:8017/service/notification/send/email \
  --header 'content-type: application/json' \
  --data '{
	"emails":[
		{		
        	"group":"rpm_notification",		
			"subject":"Testing is email subject line",
			"content":{
				"template":"This is email content with value binding ($bindingkey).",
				"bindingValues": {"bindingkey":"Binding value here"}
			}
		}
	]
}'
```

### Example: Create Notification Group

```shell
curl --request POST \
  --url http://host0001:8017/service/notification/group \
  --header 'content-type: application/json' \
  --data '{
  		"name":"rpm_notification",
  		"recipients":[
            {
                "domain":"chanro3",
                "name":"Roger Chan",
                "email":"roger.chan@example.com"
            },
            {
                "domain":"vvvba",
                "name":"Balla Vivian",
                "email":"balla.vivian@example.com",
                "isCC": true
            },
  		],
  		"organization":"example"
	}'
```

### Example: Update Notification Group

```shell
curl --request PUT \
  --url http://host0001:8017/service/notification/group \
  --header 'content-type: application/json' \
  --data '{
  		"id": "5b6d3066ccb145e627aaa622",
  		"name":"rpm_notification",
  		"recipients":[
            {
                "domain":"chanro3",
                "name":"Roger Chan",
                "email":"roger.chan@example.com"
            },
            {
                "domain":"vvvba",
                "name":"Balla Vivian",
                "email":"balla.vivian@example.com",
                "isCC": true
            },
  		],
  		"organization":"example"
	}'
```

### Example: Get Notification Group by ID

```shell
curl --request GET \
  --url http://host0001:8017/service/notification/group/{id} 
```

### Example: List Notification Group

```shell
curl --request GET \
  --url http://host0001:8017/service/notification/group
```

### Example: Delete Notification Group by ID

```shell
curl --request DELETE \
  --url http://host0001:8017/service/notification/group/{id} 
```

### 

### Emails JSON format

| Field                 | Type               | Constraint                  | Description                                            | Example                       |
| --------------------- | ------------------ | --------------------------- | ------------------------------------------------------ | ----------------------------- |
| group                 | String             | Optional                    | Receiver Group Name                                    | prm_notification              |
| from                  | String             | Optional                    | Email from (default use noreply@example.com)        |                               |
| to                    | List<String>       | Mandatory if group  is NULL | List of receiver email addresses                       | ["roger.chan@example.com"] |
| cc                    | List<String>       | Optional                    | List of cc email addresses                             | ["roger.chan@example.com"] |
| bcc                   | List<String>       | Optional                    | List of bcc email addresses                            | ["roger.chan@example.com"] |
| subject               | String             | Mandatory                   | Subject of email                                       |                               |
| content               | Object             | Mandatory                   | Email content body                                     |                               |
| content.template      | String             | Mandatory                   | Email content, also a groovy template engine template. |                               |
| content.bindingValues | Map<String,String> | Optional                    | Template engine value binding map.                     |                               |
|                       |                    |                             |                                                        |                               |

### Notification Group JSON format

| Field        | Type            | Constraint         | Description          | Example               |
| ------------ | --------------- | ------------------ | -------------------- | --------------------- |
| id           | String          | Optional (Create)  | Unique ID            |                       |
| name         | String          | Mandatory (Unique) | Group name           | rpm_notification      |
| organization | String          | Mandatory          | Group's organization | Default is example |
| createTs     | Date            | Optional           | Create TS            |                       |
| updateTs     | Date            | Optional           | Update TS            |                       |
| recipients   | List<Recipient> | Mandatory          | List of Recipient    |                       |



### Recipient Group JSON format

| Field  | Type   | Constraint | Description        | Example                   |
| ------ | ------ | ---------- | ------------------ | ------------------------- |
| domain | String | Mandatory  | User domain name   | chanro3                   |
| name   | String | Mandatory  | User full name     | Roger Chan                |
| email  | String | Mandatory  | User email address | roger.chan@example.com |
| isCC   | String | Optional   | Use CC i           |                           |

### Response

```json
// Success HTTP OK (200)
{
	"systemMessage": "Emails have been sent successfully.",
    "dataObject":{
        // Response content object
    }
}
```



## Dependency

Simple Java Mail version 5.0.6  http://www.simplejavamail.org/#/about

