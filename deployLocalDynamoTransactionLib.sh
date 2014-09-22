#!/bin/sh

echo off

# This script presumes that both git and mvn are already on your command PATH,
# and that the version there are the ones you intend to be used.

which mvn
if test $? -eq 1
then
	which git
	if test $? -eq 1
	then
		echo "Neither Maven nor Git were found through $PATH.  Please add their locations and try again."
		exit -3
	fi

	echo "Maven was not found through your PATH.  Please add its location and try again."
	exit -1
fi

which git
if test $? -eq 1
then
	echo "Git was not found through your $PATH.  Please add its location and try again."
	exit -2
fi

if test -e dynamodb-transactions
then
	echo "Git will expect to create a directory named 'dynamodb-transcations' to clone the library's patched source into, but a conflicting filesystem object already exits.  Please rename or delete it and then try running this script again."  
	exit -5
fi

git clone https://github.com/jheinnic/dynamodb-transactions
cd dynamodb-transactions

mvn clean install -Dgpg.skip=true > build.log
if test $? = 0
then
	echo "Downloaded patched dynamodb-transaction binary, compiled it, and updated the binary in your local maven repository.\n\nThis script is leaving the project files present under 'dynamodb-transactions' in case you find them useful, but the directory and its contents are no longer needed for anything related to using the transaction library.\n\nWhile you may safely run 'mvn clean', compress for archival, or destroy the directory structure altogether, if you intend to dispose of it somehow, it is suggested that you attempt to run test cases from the DynamoDB project to observe an absence of transaction errors before doing so."

	exit 0
else
	echo "mvn clean install -Dgpg.skip=true" should have built the transactions library, but it failed.  The cloned repository has been left intact to enable any attempts you which to pursue.  This script will attempt a fresh download the nexttime its launched.\n\nThe build log has been captured in the dynamodb-transactions directory as build.log""
	exit -4
fi

