#!/usr/bin/env bash

env_file="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/.env"

if [[ ! -f "$env_file" ]]; then
	printf 'Environment file not found: %s\n' "$env_file" >&2
	return 1 2>/dev/null || exit 1
fi

while IFS= read -r line || [[ -n "$line" ]]; do
	# Remove whitespace for the emptiness check; the second pattern skips lines
	# whose first non-whitespace character is a literal '#'.
	[[ -z "${line//[[:space:]]/}" || "$line" == \\#* ]] && continue

	# Capture a shell-style variable name and everything after '='. The name
	# must start with a letter/underscore, while the value may contain anything.
	# BASH_REMATCH[1] is the name and BASH_REMATCH[2] is the captured value.
	if [[ "$line" =~ ^[[:space:]]*([A-Za-z_][A-Za-z0-9_]*)[[:space:]]*=[[:space:]]*(.*)$ ]]; then
		variable_name="${BASH_REMATCH[1]}"
		value="${BASH_REMATCH[2]}"

		# Match a complete value wrapped in matching single or double quotes.
		# The .* inside the parentheses captures the text between the quotes;
		# trailing whitespace is allowed but is not included in the value.
		if [[ "$value" =~ ^\"(.*)\"[[:space:]]*$ || "$value" =~ ^\'(.*)\'[[:space:]]*$ ]]; then
			value="${BASH_REMATCH[1]}"
		else
			# For an unquoted value, keep only the first whitespace-delimited word.
			value="${value%%[[:space:]]*}"
		fi

		export "$variable_name=$value"
	fi
done < "$env_file"
