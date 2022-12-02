
## Overview
This repo provides a copy of the Dataflow template [DLPTextToBigQueryStreaming](https://github.com/GoogleCloudPlatform/DataflowTemplates/blob/main/v1/src/main/java/com/google/cloud/teleport/templates/DLPTextToBigQueryStreaming.java) with a fix to 
be able to use DLP regional endpoints instead of the `global` region as well as 
using DLP templates that are stored in regional locations.

## Deployment

#### Set variables
```
PROJECT=<GCP project used for DLP and Dataflow>
BUCKET_NAME=<bucket name without gs://. Used to stage files for the template>
TEMPLATE_NAME=DLPTextToBigQueryStreaming
DATAFLOW_REGION=<gcp region where dataflow runs>
DLP_REGION=<DLP region that contains the deidentification template and regional endpoint >

DATAFLOW_STAGING_LOCATION=gs://$BUCKET_NAME/$TEMPLATE_NAME/staging
DATAFLOW_TEMP_LOCATION=gs://$BUCKET_NAME/$TEMPLATE_NAME/temp
DATAFLOW_TEMPLATE_LOCATION=gs://$BUCKET_NAME/$TEMPLATE_NAME/template/$TEMPLATE_NAME

INPUT_CSV=<input file for the template in the form gs://path/*.csv>
BATCH_SIZE=15
DATASET=<BigQuery dataset to store the results>
DLP_DEIDENITIFICATION_TEMPLATE=projects/$PROJECT/locations/$DLP_REGION/deidentifyTemplates/<template name>
```

#### Stage the template
```
mvn compile exec:java \
     -Dexec.mainClass=com.google.cloud.pso.DLPTextToBigQueryStreaming \
     -Dexec.args="--runner=DataflowRunner \
                  --project=$PROJECT \
                  --stagingLocation=$DATAFLOW_STAGING_LOCATION \
                  --gcpTempLocation=$DATAFLOW_TEMP_LOCATION \
                  --templateLocation=$DATAFLOW_TEMPLATE_LOCATION \
                  --region=$DATAFLOW_REGION" \
     -P dataflow-runner
```

#### Copy sample input CSV file to GCS
```
gsutil cp sample_data.csv $INPUT_CSV
```

#### Run the template
```
JOB_NAME=$TEMPLATE_NAME-$USER-`date +"%Y%m%d-%H%M%S%z"`

gcloud dataflow jobs run ${JOB_NAME} \
  --gcs-location=$DATAFLOW_TEMPLATE_LOCATION \
  --region=$DATAFLOW_REGION \
  --parameters \
  "inputFilePattern=${INPUT_CSV},batchSize=${BATCH_SIZE},datasetName=${DATASET},dlpProjectId=${PROJECT},deidentifyTemplateName=${DLP_DEIDENITIFICATION_TEMPLATE},dlpRegion=${DLP_REGION}"
```