disk_nodes = []
disk_keys =  []

node[:opsworks][:layers][:rabbitmq][:instances].each do |key, values|
	disk_nodes << values[:private_dns_name]
	disk_keys  << key
end

node[:rabbitmq][:cluster_disk_nodes] = disk_nodes
