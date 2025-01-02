output "private_subnet_ids" {
  value = [aws_subnet.private_zone_1.id, aws_subnet.private_zone_2.id]
  description = "The IDs of the private subnets"
}