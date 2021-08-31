# Deploy CDS with Helm Chart

The project aims to build a sub module of the internal CRM, called "Capability Development System". It provides below major features for the employees and the managers to define the career paths and the related training plans.
The project is divided into many phases. This phase focuses on intialzing the project architecture, adding Employee Management and Resume Management.

## Architecture Overview

![alt text](./tech_docs/image/deployment.png)

## Prerequisites

- A Kubernetes Cluster (Builtin Docker Desktop)

- Docker Desktop: [docker-installer]([Docker Desktop for Mac and Windows | Docker](https://www.docker.com/products/docker-desktop))

- Helm Chart: [helm-installer]([Helm | Helm Install</title><meta charset=utf-8><meta http-equiv=x-ua-compatible content="IE=edge"><meta name=viewport content="width=device-width,initial-scale=1"><meta name=description content="Helm - The Kubernetes Package Manager."><meta property="og:title" content="Helm Install"><meta property="og:title" content="Helm Install"><meta property="og:url" content="https://helm.sh/docs/helm/helm_install/"><meta property="og:description" content="Helm - The Kubernetes Package Manager."><meta name=twitter:card content="summary"><meta name=twitter:image content="https://helm.sh/img/og-image.png"><meta name=twitter:title content="Helm | Helm Install"><meta name=twitter:description content="Helm - The Kubernetes Package Manager."><meta name=google-site-verification content="dCa3wS47cErhx0IpaxbB85NfcOP-vFxevknVk6fzf5I"><link rel=icon href=https://helm.sh/img/favicon-152.png type=image/x-icon><link rel=apple-touch-icon-precomposed href=/img/apple-touch-icon-precomposed.png><link rel=stylesheet href=/css/style.c2f168e354be12fbadf43e037e968f9a804e506040ed67dfc144e96a97ba4b0a.css integrity="sha256-wvFo41S+Evut9D4DfpaPmoBOUGBA7WffwUTpape6Swo="><link rel=stylesheet href=https://cdn.jsdelivr.net/npm/docsearch.js@2/dist/cdn/docsearch.min.css></head><body id=landing class=en></body><div id=helm class="page-docs page-wrapper page-docs-list"><header><nav class="navbar navbar-top" role=navigation aria-label="main navigation"><div class="container is-fluid is-vcentered"><div class=navbar-brand><a href=/><svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 304 351"><title>Helm](https://helm.sh/docs/helm/helm_install/))

###### Ensure you have access to Kubernetes:

```shell
kubectl get nodes -o wide
```

_Expected:_

```bash
$ kubectl get nodes
NAME             STATUS   ROLES                  AGE    VERSION
docker-desktop   Ready    control-plane,master   3d1h   v1.21.3
```

###### Install Nginx-Ingress Controller

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.48.1/deploy/static/provider/cloud/deploy.yaml
```

## Publishing image to Docker Hub

###### Build Docker Image for Backend

Go to working files

```bash
cd be/
```

Build Docker Image

```bash
docker build --rm -t $USERHUB/{your-docker-image-name}:$TAG_VERSION --no-cache -f Dockerfile .
```

---

*For example:*

```bash
docker build --rm -t $USERHUB/authentication-service:$TAG_VERSION --no-cache -f Dockerfile .
```

```bash
docker build --rm -t $USERHUB/employees-service:$TAG_VERSION --no-cache -f Dockerfile .
```

```bash
docker build --rm -t $USERHUB/admin-service:$TAG_VERSION --no-cache -f Dockerfile .
```

```bash
docker build --rm -t $USERHUB/manager-service:$TAG_VERSION --no-cache -f Dockerfile .
```

Input Required!

| Name         | Description                   |
| ------------ | ----------------------------- |
| $USERHUB     | Username of DockerHub account |
| $TAG_VERSION | Tag version of your image     |

Push image into Docker Hub

```bash
docker push $USERHUB/{your-docker-image-name}:$TAG_VERSION
```

###### Build Docker Image for Frontend

```bash
cd fe/web
npm cache clean --force
npm install --force
npm run build
docker build --rm -t $USERHUB/client-service:$TAG_VERSION --build-arg EXPOSE_PORT=3000 --no-cache -f Dockerfile .
docker push $USERHUB/client-service:$TAG_VERSION
```

```bash
#Script build all service image
#from root Folder
cd deployment
sh DockerAction.sh
```

| name          | Description                   |
|:------------- | ----------------------------- |
| \*\*\_SERVICE | name of service               |
| DIR\_\*\*     | Dir folder of service         |
| TAG_VERSION   | Tag version of your image     |
| USERHUB       | Username of DockerHub account |

> You can use Deploy_script.sh in service-folder to auto create and push image

## Hyperscale Evaluation

Helm is a package manager for Kubernetes that can be used to deploy and manage a scale-out distributed system.

###### Before Install Helm Chart

> Please customize your config image in Value.yaml

![alt text](./tech_docs/image/config_image.png)

*Ps: Override your usename of Docker Hub account in this case*

###### Deploy Database PostgreSQL with helm chart

To init and generate a database please follow these steps

```bash
#From root Folder
cd deployment
helm install postgresql-statefull postgresql/
```

###### Helm Deployment

```bash
#From root Folder
#Install authentication
cd be/authentication
helm install authentication-service deployment/
#Install employee
cd be/employee
helm install employees-service deployment/
#Install admin
cd be/admin
helm install admin-service deployment/
#Install manager
cd be/manager
helm install manager-service deployment/
#Install cdo-webclient
cd fe/
helm install cdo-webclient deployment/
```

_Expected:_

```bash
Install Authentication-service Chart
NAME: authentication-service
LAST DEPLOYED: Thu Aug 26 11:31:04 2021
NAMESPACE: default
STATUS: deployed
REVISION: 1
NOTES:
1. Get the application URL by running these commands:
  http://local.dev.mowede.com/authentication
```

To check the status of the deployment, use the following command.

```bash
kubectl get pods
```

*Expected:*

```bash
NAME                                     READY   STATUS             RESTARTS   AGE
admin-service-84f864dd65-494d6           1/1     Running            0          3m7s
authentication-service-cf4d96d6b-dhlpz   1/1     Running            0          3m11s
employees-service-69dccf58b-xb2c2        1/1     Running            0          3m9s
manager-service-85c77b97d4-h5w5t         1/1     Running            0          3m6s
postgresql-statefull-postgresql-0        1/1     Running            0          3m12s
```

> <mark>For fast, you can use script i created in the deployment folder</mark>

```bash
#Script install all Helm Chart
cd deployment
sh HelmInstall.sh
```

## Helm Cleanup

```bash
helm uninstall {chart-name}
```

## Running CDS

If everything has been set up correctly, you'll now be able to navigate to ingress nginx.

- local.dev.mowede.com

## API Ingress

- Authentication API
  
  - local.dev.mowede.com/authentication

- Employee API
  
  - local.dev.mowede.com/employees

- Admin API
  
  - local.dev.mowede.com/admin

- Manager API
  
  - local.dev.mowede.com/manager

## Generating Data

This is a easy way to create schema and data following this steps

```bash
cd deployment/postgresql/files/docker-entrypoint-initdb.d
```

_In this folder you can copy the script.sql file you want to create and it will be generating at the first time you install Postgres helm chart_

###### Note

More detail for using postgres helm chart please follow this repo:

[Postgres-HelmChart]([charts/bitnami/postgresql at master · bitnami/charts · GitHub](https://github.com/bitnami/charts/tree/master/bitnami/postgresql/#installing-the-chart))
