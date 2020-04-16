# DocDownloader

Download pdf/epub files from a page by selecting the link with a CSS class
The pages to analyze are passed in a CSV file with the name of the file;url (See sample.csv)

## Compile

    ./gradlew build

## Execute

    ./gradlew run --args="<CSV_FILE_PATH>"

It will create a docs folder to download all found files.
