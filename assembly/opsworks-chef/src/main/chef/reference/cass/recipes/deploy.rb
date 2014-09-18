include_recipe 'apt'

apt_repository 'cassandra' do
  uri 'http://www.apache.org/dist/cassandra/debian'
  distribution node['lsb']['codename']
  components ['11x']['main']
  keyserver 'pgp.mit.edu'
  key 'F758CE318D77295D'
  key '2B5C1B00'
end

# apt_repository 'cassandra-src' do
#   uri 'http://www.apache.org/dist/cassandra/debian'
#   distribution node['lsb']['codename']
#   components ['11x']['main']
#   keyserver 'pgp.mit.edu'
#   key 'F758CE318D77295D'
#   key '2B5C1B00'
# end
 
# --- Install packages we need ---
package 'cassandra'

# cookbook-file '/etc/cassandra.conf'
template '/etc/cassandra.conf' do
  source 'cassandra.erb'
end

service 'cassandra' do
  action :restart
end

