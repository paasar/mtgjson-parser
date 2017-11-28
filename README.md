# mtgjson-parser

A Clojure program to parse data from [mtgjson.com file](https://mtgjson.com/json/AllSetsArray-x.json) and write each card name in a file per block.

Output is list of files named like so `0214__XLN__Ixalan`. They are ordered by release date.
File content looks something like this:

    Adanto Vanguard;435152
    Adanto, the First Fort;435174
    Admiral Beckett Brass;435373
    Air Elemental;435197
    Ancient Brontodon;435330
    ...

## Usage

    Download [input file](https://mtgjson.com/json/AllSetsArray-x.json) to `resources` directory.
    lein run

Resulting files should be in `output` directory.

## License

Copyright © 2017 Ari Paasonen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
