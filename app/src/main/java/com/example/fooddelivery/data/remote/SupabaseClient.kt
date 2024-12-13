import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabaseClient = createSupabaseClient(
    supabaseUrl = "https://kfhcvlegzuemrxwfkgak.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtmaGN2bGVnenVlbXJ4d2ZrZ2FrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM5NTQxOTMsImV4cCI6MjA0OTUzMDE5M30.oizzttRgeJEtvcozA5fkCbmO8fynjmd4EgGNBCzYGMA"
) {
    install(Postgrest)
}
