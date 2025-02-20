group "default" {
  targets = ["frontend", "backend"]
}

target "common" {
  platforms = ["linux/amd64", "linux/arm64"]
  annotations = ["org.opencontainers.image.authors=dvdksn"]
  no-cache = true
  labels = {
    "org.opencontainers.image.source" = "https://github.com/shortthirdman/CommVault"
  }
}

variable "TAG" {
  default = "latest"
}

variable "DOCKER_REGISTRY" {
  default = "366140438193.dkr.ecr.ap-south-1.amazonaws.com"
}

target "frontend" {
  inherits = ["common"]
  context = "./commvault-ui"
  dockerfile = "Dockerfile"
  args = {
    NODE_VERSION = "22.14.0-alpine3.21"
  }
  tags = ["${DOCKER_REGISTRY}/commvault-ui:${TAG}"]
}

target "backend" {
  inherits = ["common"]
  context = "./commvault"
  dockerfile = "Dockerfile"
  tags = ["${DOCKER_REGISTRY}/commvault:${TAG}"]
}