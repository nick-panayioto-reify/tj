# TJ - Transit <-> JSON Converter

## Build
```shell 
make uberjar
```

## Usage
```
  -i, --input-type TYPE    transit  Type of input data: json, transit, or edn
  -o, --output-type TYPE   json     Type of output data: json, transit, or edn
  -e, --encoding ENCODING  json     Transit encoding
  -h, --help
```
### Convert From transit data to plain json
```shell
tj -e <transit encoding> <transit encoded data>
```

### Convert From json to transit
```shell
tj -i json -o transit -e <transit encoding> <json>
```

### Convert From json to edn
```shell
tj -i json -o edn <json>
```
