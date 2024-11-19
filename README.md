In the project root folder you will find a modified alexa-json file that has review jsons in an array and hence all reviews can be uploaded at once using the end point "/api/v1/reviews/multiple". You will also find a Postman collection that has samples for each endpoint.

I have dockerised the application along with a MySQL databse and pushed the image to Docker Hub. You can run the docker compose file (command "docker compose up") to spin up an instance of the application and database.

I have made the following assumptions while creating the application:
  - iTunes and GooglePlayStore are the only stores and no new store will be added by reviewer. Hardcoded stores as enums.
  - Considered ratings to be whole numbers and represented with integer data type.
  - Rounding off average rating value to 2 decimal places.
  - In get all reviews with date filter, one needs to input date and time and not just date.
