# Face Information Server

[![Face Information Server Docker Image](https://dockerbuildbadges.quelltext.eu/status.svg?organization=holymatch&repository=faceweb)](https://hub.docker.com/r/holymatch/faceweb/)

The source code is for my dissertations A Deep Learning Based Face Recognition Application with Augmented Reality on Microsoft Hololens. This server is used to store the person information and request [Face Engine](https://github.com/holymatch/faceengine) to recognize person. 

# Run the server
It is highly recommand to use [face system docker compose](https://github.com/holymatch/facesystem) to run the Face Information Server with the [Face Engine](https://github.com/holymatch/faceengine).

```sh
$ docker-compose up -d
```

## Setup Face Information Server and Face Engine in different hosts
To start the Face Information Server and Face Engine in different hosts, we need to add the host information of Face Engine when run the Docker. Use `--add-host=faceengine:{ip of faceenging}` to map the hostname faceengine with the IP of the Face Engine.

To keep the data permanently we can create docker volume to store the data. To create and map the volume to Docker, run the following comamand:
```sh
$ docker volume create faceweb-data-volume
```

Start the Docker with volume and hosts mapping
```sh
$ docker run -d \
  --name faceweb \
  --mount source=faceweb-data-volume,target=/Data/Database \
  --add=host=faceengine:{ip of faceengine} \
  holymatch/faceweb:latest
```
