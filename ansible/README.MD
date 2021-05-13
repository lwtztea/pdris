````
ansible-playbook -i hosts start_postgres.yml
gradle clean build -p ..
ansible-playbook -i hosts archive_images.yml
ansible-playbook -i hosts push_and_start_services.yml