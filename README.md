Shorter
===

Written by [kotlin](https://kotlinlang.org/), based on [spark](http://sparkjava.com/), [mybatis](http://www.mybatis.org/mybatis-3/)

_Given up, Just for fun_

### Run & Test

1. Init DB `src/storage/storage.sql` 

1. Create `conf/app.conf`, example in `conf/app.default.conf`

1. Run `./gradlew run`

1. Test `./gradlew test`

### API

Errors are in response's `err` json field

1. **[POST]** `/api/author`
   
   - Body:
     ```
     {
         "name": "author"
     }
     ```
   
   - Response:
     ```
     {
         "data": "init_token"
     }
     ```
     
     *Token is for publishing sheet. Once forgetting your token, you have to recreate author* 
   
1. **[POST]** `/api/sheet`

   - Header:
   
     `X-Token`: your token
   
   - Body:
     ```
     {
         "type": "TEXT | LINK",
         "text": "required",
         "link": "required if type == LINK"
     }
     ```
    
   - Response:
     ```
     {
         "data": "next_token"
     }
     ```

1. **[GET]** `/api/sheet`

   - Parameters:
   
     _offset_, _count_ for pagination
        
   - Reposne:
     ```
     {
         data: [
             {
                 id: "1e7b03e5-9820-426f-ba52-65e925f5ad54",
                 author: "authorName",
                 type: "TEXT",
                 text: "sheetText",
                 link: "sheetLink"
             },
             {
                 id: "bd9e42ad-bafe-4cf5-9320-8088f7e87255",
                 author: "authorName",
                 type: "TEXT",
                 text: "sheetText",
                 link: "sheetLink"
             }
         ]
     }
     ```
