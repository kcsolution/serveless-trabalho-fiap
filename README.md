## Aplicativo AWS SAM para gerenciamento de Serveless Country

Este é um aplicativo de amostra para demonstrar como construir um aplicativo no AWS Serverless Envinronment usando o AWS SAM, Amazon API Gateway, AWS Lambda e Amazon DynamoDB.
Ele também usa a estrutura ORM do DynamoDBMapper para mapear itens de estudo em uma tabela do DynamoDB para uma API RESTful para gerenciar paises.


## Requisitos

* AWS CLI já configurado com pelo menos com permissão de usuário root
* [Java SE Development Kit 8 installed](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Docker installed](https://www.docker.com/community-edition)
* [Maven](https://maven.apache.org/install.html)
* [SAM CLI](https://github.com/awslabs/aws-sam-cli)
* [Python 3](https://docs.python.org/3/)

## Processo de Configura

### Instalando dependencias

Usamos `maven` para instalar nossas dependências e empacotar nosso aplicativo em um arquivo JAR:

```bash
mvn install
```

### Desenvolvimento Local

**invocando a função localmente por meio do API Gateway local**

1. Inicie o DynamoDB Local em um contêiner Docker. `docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data`

2. Criando uma tabela no DynamoDB. `aws dynamodb create-table --table-name country --attribute-definitions AttributeName=pais,AttributeType=S AttributeName=cep,AttributeType=S AttributeName=cidade,AttributeType=S AttributeName=consumed,AttributeType=S --key-schema AttributeName=pais,KeyType=HASH AttributeName=cep,KeyType=RANGE --local-secondary-indexes 'IndexName=CidadeIndex,KeySchema=[{AttributeName=pais,KeyType=HASH},{AttributeName=cidade,KeyType=RANGE}],Projection={ProjectionType=ALL}' 'IndexName=consumedIndex,KeySchema=[{AttributeName=pais,KeyType=HASH},{AttributeName=consumed,KeyType=RANGE}],Projection={ProjectionType=ALL}' --billing-mode PAY_PER_REQUEST --endpoint-url http://localhost:8000`

Deletando uma tabela no DynamoDB: `aws dynamodb delete-table --table-name country --endpoint-url http://localhost:8000`

3. Start the SAM local API.
 - On a Mac: `sam local start-api --env-vars src/test/resources/test_environment_mac.json --skip-pull-image --warm-containers eager`
 - On Windows: `sam local start-api --env-vars src/test/resources/test_environment_windows.json --skip-pull-image --warm-containers eager`
 - On Linux: `sam local start-api --env-vars src/test/resources/test_environment_linux.json --skip-pull-image --warm-containers eager`
 


 OBS:  
 
 - (1) Se existir o contêiner localmente (Java8), pode usar --skip-pull-image para remover o download

 - (2) --warm-containers eager: especifica como o AWS SAM CLI gerencia os containers para cada função.
       Dois modos estão disponíveis:
       EAGER: Os contêineres para todas as funções são carregados na inicialização e persistem entre as invocações.
       LAZY: os contêineres são carregados apenas quando cada função é chamada pela primeira vez. Esses contêineres persistem para invocações adicionais.


Se o comando anterior foi executado com sucesso, agora você deve ser capaz de atingir o seguinte endpoint local para invocar as funções com raiz em `http://localhost:3000/trips/{paises}?starts=05577000&ends=06700000`.Ele deve retornar 404. Agora você pode explorar todos os terminais, use src/test/resources/Country Fiap.postman_collection.json para importar uma coleção de restos de API para o Postman.



**SAM CLI** É usado para emular Lambda e API Gateway localmente e usa o `template.yaml` para
entender como inicializar este ambiente (tempo de execução, onde está o código-fonte, etc.) - O
o trecho a seguir é o que a CLI lerá para inicializar uma API e suas rotas:



## Packaging and deployment

AWS Lambda Java runtime aceita um arquivo zip ou um arquivo JAR autônomo - usamos o último em
este exemplo. SAM usará a propriedade `CodeUri` para saber onde procurar pelo aplicativo e
dependências:

Em primeiro lugar, precisamos de um `S3 bucket` onde podemos fazer o upload de nossas funções Lambda empacotadas como ZIP antes de implantar qualquer coisa - Se você não tem um intervalo S3 para armazenar artefatos de código, conforme exemplo abaixo:

```bash
export BUCKET_NAME=my-serveless-country-rofk-cld23
aws s3 mb s3://$BUCKET_NAME
```

Em seguida, executamos o seguinte comando para empacotar nossa função Lambda para S3:

```bash
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket $BUCKET_NAME
```

Em seguida, o comando a seguir criará um Cloudformation Stack e deploy nos recursos SAM.
```bash
sam deploy \
    --template-file packaged.yaml \
    --stack-name country-fiap \
    --capabilities CAPABILITY_IAM
```

Após a conclusão da implantação, você pode executar o seguinte comando para recuperar o URL do endpoint do gateway de API:

```bash
aws cloudformation describe-stacks \
    --stack-name scountry-fiap \
    --query 'Stacks[].Outputs'
```

## Comandos AWS CLI

Comandos AWS CLI para empacotar, implantar e descrever saídas definidas na stak cloudformation:

```bash
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket my-serveless-country-rofk-cld23

sam deploy \
    --template-file packaged.yaml \
    --stack-name country-fiap \
    --capabilities CAPABILITY_IAM \
    --parameter-overrides MyParameterSample=MySampleValue

aws cloudformation describe-stacks \
    --stack-name country-fiap --query 'Stacks[].Outputs'
```

