# mysqldump-to-redshift 0.3.1

A simple script to convert from a mysqldump file of a single table to an "importable" Redshift data file.

## Installation

Make sure to have at least Java 1.7 on the target machine

### Build the project

    $ lein uberjar

Now copy the standalone jar file to the target machine.

### Run the Tests

    $ lein tests

These currently include tests for fix-format and extract-values.  In development these were the most likely places to break by far.

### Prebuilt

Copy the prebuilt jar file in target to the target machine and execute according to the usage below.

## Usage

    $ java -jar mysqldump-to-redshift.jar [mysql dump file] [output_file]

Please note: This will overwrite `output_file`.

Once the script finishes, use s3cmd to copy the file to an S3 bucket (if the file is large, it is recommended to gzip the file.

    $ s3cmd put output_file s3://your-bucket

Once that's done, use a version of the following command to load it into your table.

    COPY [table name] from 's3://your-bucket/output_file' credentials 'aws_access_key_id=[key];aws_secret_access_key=[secret key]' delimiter '\t' EMPTYASNULL;

Remember to add the `GZIP` parameter if you've gzipped the file before loading to s3.

## Options

* `mysql dump file` - The "input" file for this script.  Should have insert statements.
* `output file` - This is the file that will be eventually loaded into Redshift

## Examples

    $ java -jar mysqldump-to-redshift.jar mysqldump_table.sql redshift_import.tsv

    $ java -jar mysqldump-to-redshift.jar mysqldump_table.sql redshift_import.tsv && gzip redshift_import.tsv && s3cmd put redshift_import.tsv.gz s3://your-bucket

## Warnings

* Only outputs tab separated values, if your data includes these, the Redshift import will break.

## Roadmap

* Determine options for open-sourcing
* Provide a command line option for delimiter
* Create a mysqldump interface (skip the writing to disk of the dump and go straight to the end result)
* Create a s3 interface
* Create a Redshift interface to fire when the s3 upload it complete
* Output to multiple files to increase performance on the Redshift load side

## License

Copyright © 2016 Bryan Lott

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
