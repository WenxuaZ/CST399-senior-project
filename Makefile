all: build

build:
	mvn clean package

run-center:
	java -cp target/P2P-1.0.jar com.run.Runner center

run-uploader:
	java -cp target/P2P-1.0.jar com.run.Runner uploader

run-downloader:
	java -cp target/P2P-1.0.jar com.run.Runner downloader

