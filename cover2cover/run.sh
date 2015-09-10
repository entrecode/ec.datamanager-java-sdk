#!/bin/bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
python $DIR/cover2cover.py $DIR/../build/reports/jacoco/test/jacocoTestReport.xml > $DIR/../build/reports/jacoco/test/cobertura.xml
python -m cobertura_clover_transform.converter $DIR/../build/reports/jacoco/test/cobertura.xml > $DIR/../build/reports/jacoco/test/clover.xml