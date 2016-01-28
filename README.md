# mysqldump-to-redshift

A simple script to convert from a mysqldump file to an "importable" Redshift data file.

## Installation

Make sure to have at least Java 1.7 on the target machine

### Build the project

    $ lein uberjar

Now copy the standalone jar file to the target machine.

### Prebuilt

Copy the prebuilt jar file in target to the target machine and execute according to the usage below.

## Usage

    $ java -jar mysqldump-to-redshift-0.1.0-standalone.jar [mysql dump file] [output file]

Once the script finishes, use s3cmd to copy the file to an S3 bucket.  Once that's done, use a version of the following command to load it into your table.

    COPY [table name] from 's3://[bucket]/[file path]' credentials 'aws_access_key_id=[key];aws_secret_access_key=[secret key]' delimiter ',' null as '\000';

## Options

mysql dump file - The "input" file for this script.  Should have insert statements.
output file - This is the file that will be eventually loaded into Redshift

## Examples

    $ java -jar mysqldump-to-redshift-0.1.0-standalone.jar

## Bugs

* Only outputs comma separated values, if your data includes these, the Redshift import will break.

## Roadmap

* Create unit tests
* Provide a command line option for delimiter
* Create a mysqldump interface (skip the writing to disk of the dump and go straight to the end result)
* Create a s3 interface
* Create a Redshift interface to fire when the s3 upload it complete
* Determine the best threading strategy
* Output to multiple files to increase performance on the Redshift load side

## License

Copyright © 2016 Bryan Lott

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
