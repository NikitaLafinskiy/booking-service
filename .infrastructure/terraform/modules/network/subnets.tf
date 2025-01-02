resource "aws_subnet" "private_zone_1" {
  vpc_id     = aws_vpc.main.id
  cidr_block = "10.0.0.0/19"
  availability_zone = var.zone1

  tags = {
    Name = "${var.environment}-private-${var.zone1}"
    "kubernetes.io/cluster/${var.environment}-${var.eks_name}" = "owned"
  }
}

resource "aws_subnet" "private_zone_2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.32.0/19"
  availability_zone = var.zone2

  tags = {
    Name                                                           = "${var.environment}-private-${var.zone2}"
    "kubernetes.io/cluster/${var.environment}-${var.eks_name}" = "owned"
  }
}

resource "aws_subnet" "public_zone_1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.64.0/19"
  availability_zone = var.zone1
  map_public_ip_on_launch = true

  tags = {
    Name                                                           = "${var.environment}-public-${var.zone1}"
    "kubernetes.io/role/elb" = "1"
    "kubernetes.io/cluster/${var.environment}-${var.eks_name}" = "owned"
  }
}

resource "aws_subnet" "public_zone_2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.96.0/19"
  availability_zone = var.zone2
  map_public_ip_on_launch = true

  tags = {
    Name                                                           = "${var.environment}-public-${var.zone2}"
    "kubernetes.io/role/elb" = "1"
    "kubernetes.io/cluster/${var.environment}-${var.eks_name}" = "owned"
  }
}