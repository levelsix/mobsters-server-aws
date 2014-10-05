disk_nodes = []
disk_keys  = []

node[:opsworks][:layers][:rabbitmq][:instances].each do |key, values|
	disk_nodes << values[:private_dns_name]
	disk_keys  << key
end

normal[:rabbitmq][:cluster] = true
normal[:rabbitmq][:cluster_disk_nodes] = disk_nodes

normal[:rabbitmq][:max_file_descriptors] = 8192
normal[:rabbitmq][:web_console_ssl] = true
normal[:rabbitmq][:web_console_ssl_port] = 15672
normal[:rabbitmq][:enabled_plugins][0] = "rabbitmq_web_stomp"
normal[:rabbitmq][:enabled_plugins][1] = "rabbitmq_management"
normal[:rabbitmq][:enabled_plugins][2] = "rabbitmq_management_agent"
normal[:rabbitmq][:enabled_plugins][3] = "webmachine"
normal[:rabbitmq][:disabled_plugins][0] = "rabbitmq_management_visualizer"
normal[:rabbitmq][:policies][:clientQueue][:pattern] = "^client.*"
normal[:rabbitmq][:policies][:clientQueue][:params][:expires] = 540000
normal[:rabbitmq][:policies][:clientQueue][:priority] = 0
normal[:rabbitmq][:policies][:clientQueue][:vhost] = "prodmobsters"
normal[:rabbitmq][:virtualhosts][0] = "prodmobsters"
normal[:rabbitmq][:enabled_users][0][:name] = "lvl6admin"
normal[:rabbitmq][:enabled_users][0][:password] = "BuR4C0JQ21Va5gIRblTg"
normal[:rabbitmq][:enabled_users][0][:rights][0][:vhost] = "/"
normal[:rabbitmq][:enabled_users][0][:rights][0][:conf] = ".*"
normal[:rabbitmq][:enabled_users][0][:rights][0][:read] = ".*"
normal[:rabbitmq][:enabled_users][0][:rights][0][:write] = ".*"
normal[:rabbitmq][:enabled_users][0][:rights][1][:vhost] = "prodmobsters"
normal[:rabbitmq][:enabled_users][0][:rights][1][:conf] = ".*"
normal[:rabbitmq][:enabled_users][0][:rights][1][:read] = ".*"
normal[:rabbitmq][:enabled_users][0][:rights][1][:write] = ".*"
normal[:rabbitmq][:enabled_users][1][:name] = "lvl6server"
normal[:rabbitmq][:enabled_users][1][:password] = "tkb4iFCDtFW3roR6Fq8n"
normal[:rabbitmq][:enabled_users][1][:rights][0][:vhost] = "prodmobsters"
normal[:rabbitmq][:enabled_users][1][:rights][0][:conf] = ".*"
normal[:rabbitmq][:enabled_users][1][:rights][0][:read] = ".*"
normal[:rabbitmq][:enabled_users][1][:rights][0][:write] = ".*"
normal[:rabbitmq][:enabled_users][2][:name] = "lvl6client"
normal[:rabbitmq][:enabled_users][2][:password] = "PMcGQFfQuy9gbX84e2UH"
normal[:rabbitmq][:enabled_users][2][:rights][0][:vhost] = "prodmobsters"
normal[:rabbitmq][:enabled_users][2][:rights][0][:conf] = "client.*"
normal[:rabbitmq][:enabled_users][2][:rights][0][:read] = "client.*"
normal[:rabbitmq][:enabled_users][2][:rights][0][:write] = "gameMessages"

# Unused and for disabling
# normal[:rabbitmq][:disabled_policies][0] = ""
# normal[:rabbitmq][:disabled_virtualhosts][0] = ""
# normal[:rabbitmq][:disabled_users][0] = ""

