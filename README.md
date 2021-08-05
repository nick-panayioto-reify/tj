# TJ - Transit <-> JSON Converter

## Build
```shell 
clojure -X:uberjar :jar tj.jar :main-class core
```

## Usage
### Convert From transit data to plain json
```shell
tj -t <transit encoded> -e <transit encoding>
```

### Convert From json to transit
```shell
tj -j <json> -e <transit encoding>
```
