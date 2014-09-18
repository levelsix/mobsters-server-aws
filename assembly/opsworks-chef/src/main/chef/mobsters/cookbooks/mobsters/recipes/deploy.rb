print node[:opsworks][:instance][:layers]

if node[:opsworks][:instance][:layers].include? :'java-app' 
	node[:deploy].each do |application, deploy|
		template File.join(node[:opsworks_java][:java_app_server][:context_dir], "#{application}.properties") do
		   	source 'mobsters.properties.erb'
		end
	end
end
