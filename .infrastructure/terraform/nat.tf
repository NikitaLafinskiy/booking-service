resource "aws_eip" "nat" {
  domain = "vpc"
  tags = {
    Name = "${local.environment}-nat"
  }
}

resource "aws_nat_gateway" "nat" {
  allocation_id = aws_eip.nat.id
  subnet_id     = aws_subnet.public_zone_1.id

  tags = {
    Name = "${local.environment}-nat"
  }

  depends_on = [aws_internet_gateway.igw]
}