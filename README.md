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

## Execution
```shell
./bin/tj <args>
```
or directly
```shell
java -jar tj.jar <args>
```

### Convert from transit data to plain json
```shell
tj -e <transit encoding> <transit encoded data>
```

### Convert from json to transit
```shell
tj -i json -o transit -e <transit encoding> <json>
```

### Convert from json to edn
```shell
tj -i json -o edn <json>
```

## Emacs
Make sure the [tj](./bin/tj) script is accessable from emacs' shell
```lisp
(add-to-list 'exec-path <bin dir>)
```
and evaluate [tj.el](./tj.el)
