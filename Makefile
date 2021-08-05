
uberjar:
	@clojure -X:uberjar :jar tj.jar :main-class core

clean:
	@rm -rf ./target
	@rm ./tj.jar
