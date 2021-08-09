
.PHONY: tj.jar clean

tj.jar:
	@clojure -X:uberjar :jar tj.jar :main-class core

clean:
	@rm ./tj.jar
